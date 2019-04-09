#include <stdint.h>

namespace art {
    namespace mirror {
        class Object {
            public:
                uint32_t klass_;
                uint32_t monitor_;
        };
        class ArtMethod:public Object {
            public:
                uint32_t declaring_class_;
                uint32_t dex_cache_resolved_methods_;
                uint32_t dex_cache_resolved_types_;
                uint32_t dex_cache_strings_;

                uint64_t entry_point_from_interpreter_;
                uint64_t entry_point_from_jni_;
                uint64_t entry_point_from_quick_compiled_code_;
                uint64_t gc_map_;

                uint32_t access_flags_;
                uint32_t dex_code_item_offset_;
                uint32_t dex_method_index_;
                uint32_t method_index_;

        };
    }
}