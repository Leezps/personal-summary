package com.leezp.testnotification.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.leezp.testnotification.R;
import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.services.MultiDownloadService;

import java.util.List;

/**
 * 文件列表适配器
 */
public class FileListAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<FileInfo> mFileList = null;

    public FileListAdapter(Context mContext, List<FileInfo> mFileList) {
        this.mContext = mContext;
        this.mFileList = mFileList;
    }

    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = mFileList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            //加载视图
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            //获得布局中的控件
            holder = new ViewHolder();
            holder.tvFile = convertView.findViewById(R.id.tvFileName);
            holder.btStop = convertView.findViewById(R.id.buttonStop);
            holder.btStart = convertView.findViewById(R.id.buttonStart);
            holder.pbFile = convertView.findViewById(R.id.pbProgress);
            holder.tvFile.setText(fileInfo.getFileName());
            holder.pbFile.setMax(100);
            holder.btStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通知Service开始下载
                    Intent intent = new Intent(mContext, MultiDownloadService.class);
                    intent.setAction(MultiDownloadService.ACTION_START);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            holder.btStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通知Service停止下载
                    Intent intent = new Intent(mContext, MultiDownloadService.class);
                    intent.setAction(MultiDownloadService.ACTION_STOP);
                    intent.putExtra("fileInfo",fileInfo);
                    mContext.startService(intent);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置视图中的控件
        holder.pbFile.setProgress(fileInfo.getFinished());
        return convertView;
    }

    /**
     * 更新列表项中的进度条
     */
    public void updateProgress(int id,int progress) {
        FileInfo fileInfo = mFileList.get(id);
        fileInfo.setFinished(progress);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvFile;
        Button btStop,btStart;
        ProgressBar pbFile;
    }
}
