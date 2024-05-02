<script setup>
import {Document, Upload} from "@element-plus/icons-vue";
import {reactive} from "vue";
import {Quill, QuillEditor} from "@vueup/vue-quill";
import ImageResize from "quill-image-resize-vue"
import {ImageExtend, QuillWatch} from "quill-image-super-solution-module"
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import axios from "axios";
import {accessHeader} from "@/net";
import {ElMessage} from "element-plus";

Quill.register('modules/ImageResize', ImageResize)
Quill.register('modules/ImageExtend', ImageExtend)

defineProps({
  show: Boolean
})

const editor = reactive({
  type: null,
  title: '',
  text: '',
  loading: false
})

const emit = defineEmits(['close'])

const types = [
  {id: 1, name: '日常闲聊', desc: '在这里分享你的各种日常'},
  {id: 2, name: '真诚交友', desc: '在校园里寻找和你志同道合的朋友'},
  {id: 3, name: '问题反馈', desc: '反馈你在校园中遇到的各种问题'},
  {id: 4, name: '恋爱官宣', desc: '向大家展示你的恋爱成果'},
  {id: 5, name: '踩坑记录', desc: '将你遇到过的坑分享给大家，防止别人再次踩坑'},
]

const editorOption = {
  modules: {
    toolbar: {
      container: [
        "bold", "italic", "underline", "strike", "clean",
        {color: []}, {'background': []},
        {size: ["small", false, "large", "huge"]},
        {header: [1, 2, 3, 4, 5, 6, false]},
        {list: "ordered"}, {list: "bullet"}, {align: []},
        "blockquote", "code-block", "link", "image",
        {indent: '-1'}, {indent: '+1'}
      ],
      handlers: {
        'image': function () {
          QuillWatch.emit(this.quill.id)
        }
      }
    },
    imageResize: {
      modules: ['Resize', 'DisplaySize']
    },
    ImageExtend: {
      action: axios.defaults.baseURL + '/api/image/cache',
      name: 'file',
      size: 5,
      loading: true,
      accept: 'image/png, image/jpeg',
      response: (resp) => {
        if (resp.data) {
          return axios.defaults.baseURL + '/images' + resp.data
        } else {
          return null
        }
      },
      methods: 'POST',
      headers: xhr => {
        xhr.setRequestHeader('Authorization', accessHeader().Authorization);
      },
      start: () => editor.loading = true,
      success: () => {
        ElMessage.success('图片上传成功！')
        editor.loading = false
      },
      error: () => {
        ElMessage.warning('图片上传失败，请联系管理员！')
        editor.loading = false
      }
    }
  }
}
</script>


<template>
  <div>
    <el-drawer :model-value="show"
               direction="btt"
               :size="620"
               :close-on-click-modal="false"
               @close="emit('close')">
      <template #header>
        <div>
          <div style="font-weight: bold">发表新的帖子</div>
          <div style="font-size: 13px">发表内容请遵守法律法规！</div>
        </div>
      </template>

      <div style="display:flex; gap: 10px">
        <div style="width: 150px">
          <el-select placeholder="请选择主题类型..." v-model="editor.type">
            <el-option v-for="item in types" :value="item.id" :label="item.name"/>
          </el-select>
        </div>
        <div style="flex: 1">
          <el-input v-model="editor.title" placeholder="请输入帖子标题..." :prefix-icon="Document"
                    style="height: 100%;"/>
        </div>
      </div>

      <div style="margin-top: 15px; height: 430px; overflow: hidden; border-radius: 5px"
           v-loading="editor.loading" element-loading-text="正在上传图片，请稍后...">
        <quill-editor v-model:content="editor.text" style="height: calc(100% - 45px)"
                      content-type="delta"
                      placeholder="向大家分享点什么......" :options="editorOption"/>
      </div>

      <div style="display: flex;justify-content: space-between;margin-top: 10px;">
        <div style="color: grey;font-size: 13px">
          当前字数666（最大支持20000字）
        </div>

        <div>
          <el-button type="success" :icon="Upload" plain>立即发表主题</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>


<style scoped>
:deep(.el-drawer) {
  width: 800px;
  margin: auto;
  border-radius: 15px 15px 0 0;
}

:deep(.el-drawer__header) {
  margin: 0;
}

:deep(.ql-toolbar) {
  border-radius: 10px 10px 0 0;
  border-color: var(--el-border-color);
}

:deep(.ql-container) {
  border-radius: 0 0 10px 10px;
  border-color: var(--el-border-color);
}

:deep(.ql-editor.ql-blank::before) {
  color: var(--el-text-color-placeholder);
  font-style: normal;
}

:deep(.ql-editor) {
  font-size: 14px;
}
</style>
