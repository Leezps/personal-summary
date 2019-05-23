package com.leezp.complier;

import com.leezp.annotation.NeedsPermission;
import com.leezp.annotation.OnNeverAskAgain;
import com.leezp.annotation.OnPermissionDenied;
import com.leezp.annotation.OnShowRationale;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

public class PermissionProcessor extends AbstractProcessor {

    private Messager messager;  // 用来报告错误，警告，提示
    private Elements elementsUtils; // 包含了很多的操作Elements的工具方法
    private Filer filer;    // 用来创建新的源文件，Class文件(造币技术)
    private Types typeUtils;    // 包含用于操作TypeMirror工具方法

    //获取支持注解的类型
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(NeedsPermission.class.getCanonicalName());
        types.add(OnNeverAskAgain.class.getCanonicalName());
        types.add(OnPermissionDenied.class.getCanonicalName());
        types.add(OnShowRationale.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        // 返回注解支持的最新源版本，JDK
        // 也可使用@SupportedSourceVersion(SourceVersion.RELEASE_8)此种方式表达
        return SourceVersion.latestSupported();
    }

    // 初始化工作
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        typeUtils = processingEnvironment.getTypeUtils();
        elementsUtils = processingEnvironment.getElementUtils();
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
    }

    // 注解处理器的核心方法，处理具体的注解时限，生成 java 代码
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 获取MainActivity中所有带NeedsPermission注解的方法
        Set<? extends Element> needsPermissionSet = roundEnvironment.getElementsAnnotatedWith(NeedsPermission.class);
        // 保存起来，键值对：Key com.xxx.MainActivity value所有带NeedsPermission注解的方法
        Map<String, List<ExecutableElement>> needsPermissionMap = new HashMap<>();
        // 遍历所有的NeedsPermission注解的方法
        for (Element element : needsPermissionSet) {
            // 转换成方法元素（结构体元素）
            ExecutableElement executableElement = (ExecutableElement) element;
            // 通过方法元素获取它所属的MainActivity类名，如：com.leezp.note_permission_sample.MainActivity
            String activityName = getActivityName(executableElement);
            // 从缓存集合中获取MainActivity所有带NeedsPermission注解的方法集合
            List<ExecutableElement> list = needsPermissionMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                // 先加入map集合，引用变量list可以动态改变值
                needsPermissionMap.put(activityName, list);
            }
            // 将MainActivity所有带NeedsPermission注解的方法加入到list集合
            list.add(executableElement);
        }

        // 获取MainActivity中所有带OnNeverAskAgain注解的方法
        Set<? extends Element> onNeverAskAgainSet = roundEnvironment.getElementsAnnotatedWith(OnNeverAskAgain.class);
        Map<String, List<ExecutableElement>> onNeverAskAgainMap = new HashMap<>();
        for (Element element : onNeverAskAgainSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onNeverAskAgainMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                onNeverAskAgainMap.put(activityName, list);
            }
            list.add(executableElement);
        }

        // 获取MainActivity中所有带OnPermissionDenied注解的方法
        Set<? extends Element> onPermissionDeniedSet = roundEnvironment.getElementsAnnotatedWith(OnPermissionDenied.class);
        Map<String, List<ExecutableElement>> onPermissionDeniedMap = new HashMap<>();
        for (Element element : onPermissionDeniedSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onPermissionDeniedMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                onPermissionDeniedMap.put(activityName, list);
            }
            list.add(executableElement);
        }

        // 获取MainActivity中所有带OnShowRationale注解的方法
        Set<? extends Element> onShowRationaleSet = roundEnvironment.getElementsAnnotatedWith(OnShowRationale.class);
        Map<String, List<ExecutableElement>> onShowRationaleMap = new HashMap<>();
        for (Element element : onShowRationaleSet) {
            ExecutableElement executableElement = (ExecutableElement) element;
            String activityName = getActivityName(executableElement);
            List<ExecutableElement> list = onShowRationaleMap.get(activityName);
            if (list == null) {
                list = new ArrayList<>();
                onShowRationaleMap.put(activityName, list);
            }
            list.add(executableElement);
        }

        // -------------------------- 造人民币了 --------------------------
        // 获取Activity完整的字符串类名（包名 + 类名）
        for (String activityName : needsPermissionMap.keySet()) {
            // 获取“com.leezp.note_permission_sample.MainActivity”中所有控件方法的集合
            List<ExecutableElement> needPermissionElements = needsPermissionMap.get(activityName);
            List<ExecutableElement> onNeverAskAgainElements = onNeverAskAgainMap.get(activityName);
            List<ExecutableElement> onPermissionDeniedElements = onPermissionDeniedMap.get(activityName);
            List<ExecutableElement> onShowRationaleElements = onShowRationaleMap.get(activityName);

            final String CLASS_SUFFIX = "$Permissions";
            Filer filer = processingEnv.getFiler();
            try {
                // 创建一个新的源文件class，并返回一个对象数组以允许写入它(MainActivity$Permission)
                JavaFileObject javaFileObject = filer.createSourceFile(activityName + CLASS_SUFFIX);
                // 通过方法标签获取包名标签（任意一个属性标签的父节点都是同一个包名）
                String packageName = getPackageName(needPermissionElements.get(0));

                // 定义Writer对象，开启造币过程
                Writer writer = javaFileObject.openWriter();

                // 类名：MainActivity$Permissions，不是com.leezp.note_permission_sample.MainActivity$Permissions
                // 通过属性元素获取它所属的MainActivity类名，再拼接后结果为：MainActivity$Permissions
                String activitySimpleName = needPermissionElements.get(0).getEnclosingElement().getSimpleName().toString() + CLASS_SUFFIX;

                // 生成包
                writer.write("package " + packageName + ";\n");
                // 生成要导入的接口类（必须手动导入）
                writer.write("import com.leezp.library.listener.RequestPermission;\n");
                writer.write("import com.leezp.library.listener.PermissionRequest;\n");
                writer.write("import com.leezp.library.utils.PermissionUtils;\n");
                writer.write("import android.support.v7.app.AppCompatActivity;\n");
                writer.write("import android.support.v4.app.ActivityCompat;\n");
                writer.write("import android.support.annotation.NonNull;\n");
                writer.write("import java.lang.ref.WeakReference;\n");

                // 生成类
                writer.write("public class " + activitySimpleName + " implements RequestPermission<" + activityName + "> {\n");

                // 生成常量属性
                writer.write("private static final int REQUEST_SHOWCAMERA = 666;\n");
                writer.write("private static String[] PERMISSION_SHOWCAMERA;\n");

                // 生成requestPermission 方法
                writer.write("public void requestPermission(" + activityName + " target, String[] permissions) {\n");
                writer.write("PERMISSION_SHOWCAMERA = permissions;\n");
                writer.write("if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {\n");
                // 循环生成MainActivity每个权限申请方法
                for (ExecutableElement executableElement : needPermissionElements) {
                    // 获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    // 调用申请权限方法
                    writer.write("target." + methodName + "();\n");
                }
                writer.write("} else if(PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {\n");
                // 循环生成 MainActivity 每个提示用户为何要开启权限方法
                if (onShowRationaleElements != null && !onShowRationaleElements.isEmpty()) {
                    for (ExecutableElement executableElement : onShowRationaleElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用提示用户为何要开启权限方法
                        writer.write("target." + methodName + "(new PermissionRequestImpl(target));\n");
                    }
                }
                writer.write("} else {\n");
                writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n}\n}\n");

                // 生成onRequestPermissionResult方法
                writer.write("public void onRequestPermissionsResult(" + activityName + " target, int requestCode, @NonNull int[] grantResults) {\n");
                writer.write("switch(requestCode) {\n");
                writer.write("case REQUEST_SHOWCAMERA:\n");
                writer.write("if (PermissionUtils.verifyPermissions(grantResults)) {\n");
                // 循环生成MainActivity 每个权限申请方法
                for (ExecutableElement executableElement : needPermissionElements) {
                    // 获取方法名
                    String methodName = executableElement.getSimpleName().toString();
                    // 调用申请权限方法
                    writer.write("target." + methodName + "();\n");
                }
                writer.write("} else if(!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {\n");
                //循环生成MainActivity每个不再询问后的提示
                if (onNeverAskAgainElements != null && !onNeverAskAgainElements.isEmpty()) {
                    for (ExecutableElement executableElement : onNeverAskAgainElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用不再询问后的提示
                        writer.write("target." + methodName + "();\n");
                    }
                }
                writer.write("} else {\n");
                // 循环生成MainActivity每个拒绝时的提示方法
                if (onPermissionDeniedElements != null && !onPermissionDeniedElements.isEmpty()) {
                    for (ExecutableElement executableElement : onPermissionDeniedElements) {
                        // 获取方法名
                        String methodName = executableElement.getSimpleName().toString();
                        // 调用拒绝时的提示方法
                        writer.write("target." + methodName + "();\n");
                    }
                }
                writer.write("}\nbreak;\ndefault:\nbreak;\n}\n}\n");

                //生成接口实现类：PermissionRequestImpl implements PermissionRequest
                writer.write("private static final class PermissionRequestImpl implements PermissionRequest {\n");
                writer.write("private final WeakReference<" + activityName + "> weakTarget;\n");
                writer.write("private PermissionRequestImpl("+activityName+" target) {\n");
                writer.write("this.weakTarget = new WeakReference(target);\n}\n");
                writer.write("public void proceed() {\n");
                writer.write(activityName+" target = ("+activityName+")this.weakTarget.get();\n");
                writer.write("if(target != null) {\n");
                writer.write("ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);\n}\n}\n}\n");

                // 最后结束标签，造币完成
                writer.write("\n}");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String getActivityName(ExecutableElement executableElement) {
        // 获取方法标签获取类名标签，再获取类名称获取包名标签
        String packageName = getPackageName(executableElement);
        // 通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        // 完整字符串拼接：com.leezp.note_permission_sample+"."+MainActivity
        return packageName + "." + typeElement.getSimpleName().toString();
    }

    private String getPackageName(ExecutableElement executableElement) {
        // 通过方法标签获取类名标签
        TypeElement typeElement = (TypeElement) executableElement.getEnclosingElement();
        // 通过类名标签获取包名标签
        String packageName = processingEnv.getElementUtils().getPackageOf(typeElement).getQualifiedName().toString();
        System.out.println("packageName >>> " + packageName);
        return packageName;
    }
}
