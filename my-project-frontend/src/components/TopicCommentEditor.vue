<script setup>
import {Delta, QuillEditor} from "@vueup/vue-quill";
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import {computed, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const props = defineProps({
  show: Boolean,
  tid: String,
  quote: Object,
})

const content = ref()

const emit = defineEmits(['close', 'comment'])

const init = () => content.value = new Delta()

function submitComment() {
  if (deltaToText(content.value).length > 2000) {
    ElMessage.warning('评论字数已超出限制！请删减后再试')
    return
  }
  if (deltaToText(content.value).length === 0) {
    ElMessage.warning('请输入评论内容！')
    return
  }
  post('api/forum/add-comment', {
    tid: props.tid,
    quote: props.quote ? props.quote.id : -1,
    content: JSON.stringify(content.value)
  }, () => {
    ElMessage.success('发表评论成功')
    emit('comment')
  })
}

function deltaToSimpleText(delta) {
  let str = deltaToText(JSON.parse(delta))
  if (str.length > 35) str = str.substring(0, 35) + "..."
  return str
}

function deltaToText(delta) {
  if (!delta?.ops) return ""
  let str = ""
  for (let op of delta.ops) {
    str += op.insert
  }
  return str.replace(/\s/g, "")
}
</script>

<template>
  <div>
    <el-drawer :model-value="show" @close="emit('close')" direction="btt"
               :size="270" :close-on-click-modal="false"
               :title="quote ? `发表对评论：“${deltaToSimpleText(quote.content)}” 的回复` : '回复帖子'"
               @open="init">
      <div>
        <div>
          <quill-editor style="height: 120px" v-model:content="content" placeholder="留下一条友善的评论吧..."/>
        </div>

        <div style="margin: 10px 0 0 3px; display: flex;">
          <div style="flex: 1; font-size: 13px; color: grey">
            当前字数：{{ deltaToText(content).length }} 字（最大支持2000字）
          </div>
          <el-button @mousedown="e => e.preventDefault()" @click="submitComment" type="success">发表评论</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<style lang="less" scoped>
:deep(.el-drawer) {
  width: 800px;
  margin: 20px auto;
  border-radius: 10px;
}

:deep(.el-drawer__header) {
  margin: 0;
}

:deep(.el-drawer__body) {
  padding: 10px;
}
</style>
