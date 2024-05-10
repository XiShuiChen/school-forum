<script setup>

import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net";
import {ElMessage} from "element-plus";
import TopicEditor from "@/components/TopicEditor.vue";

const today = computed(() => {
  const date = new Date()
  return `${date.getFullYear()} 年 ${date.getMonth() + 1} 月 ${date.getDate()} 日`
})

const weather = reactive({
  location: {},
  now: {},
  hourly: {},
  success: false
})

const editor = ref(false)

navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  get(`api/forum/weather?longitude=${longitude}&latitude=${latitude}`, data => {
    Object.assign(weather, data)
    weather.success = true
  })
}, error => {
  console.info(error)
  ElMessage.warning('位置信息获取超时，请检测网络设置')
  get('/api/forum/weather?longitude=113.68529&latitude=34.58739', data => {
    Object.assign(weather, data)
    weather.success = true
  })
}, {
  timeout: 3000,
  enableHighAccuracy: true
})

</script>

<template>
  <div style="display: flex; margin: 20px auto; gap: 20px; max-width: 900px">
    <div style="flex: 1">
      <light-card>
        <div class="create-topic" @click="editor = true">
          <el-icon>
            <EditPen/>
          </el-icon>
          点击发表主题...
        </div>
      </light-card>

      <light-card style="margin-top: 10px; height: 30px">

      </light-card>

      <div style="margin-top: 10px; display: flex; flex-direction: column;gap: 10px">
        <light-card style="height:150px" v-for="item in 10">
        </light-card>
      </div>
    </div>

    <div style="width: 280px">
      <div style="position: sticky; top: 20px">
        <light-card>
          <div style="font-weight: bold">
            <el-icon>
              <CollectionTag/>
            </el-icon>
            论坛公告
          </div>

          <el-divider style="margin: 10px 0"/>

          <div style="font-size: 14px; margin: 10px; color: grey">
            这件事拥护的人不多，反对的人不少。这两件事都没有完。这笔“遗产”得交给下一代。
            怎么交？和平交不成就动荡中交，搞不好就得“血雨腥风”了。你们怎么办？只有天知道。
          </div>
        </light-card>

        <light-card style="margin-top:10px">
          <div style="font-weight: bold">
            <el-icon>
              <Calendar/>
            </el-icon>
            天气信息
          </div>

          <el-divider style="margin: 10px 0"/>

          <weather :data="weather"/>

        </light-card>

        <light-card style="margin-top: 10px">
          <div class="info-text">
            <div>当前日期</div>
            <div>{{ today }}</div>
          </div>

          <div class="info-text">
            <div>当前IP地址</div>
            <div>127.0.0.1</div>
          </div>
        </light-card>

        <div style="font-size: 14px; margin-top: 10px; color: grey">
          <el-icon>
            <Link/>
          </el-icon>
          友情链接
          <el-divider style="margin:10px 0"/>
        </div>
        <div style="display: grid; grid-template-columns: repeat(2, 1fr); grid-gap: 10px; margin-top: 10px">
          <div class="friend-link">
            <el-image style="height: 80px; width: 140px"
                      src="https://lupic.cdn.bcebos.com/20220708/3087023372_14_600_429.jpg"/>
          </div>

          <div class="friend-link">
            <el-image style="height: 80px; width: 140px"
                      src="https://lupic.cdn.bcebos.com/20220822/3087679787_14_561_400.jpg"/>
          </div>
        </div>
      </div>
    </div>
    <topic-editor :show="editor" @close="editor = false"/>
  </div>
</template>

<style lang="less" scoped>
.info-text {
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px
}

.friend-link {
  border-radius: 5px;
  overflow: hidden;
}

.create-topic {
  background-color: #efefef;
  border-radius: 5px;
  height: 40px;
  color: grey;
  font-size: 14px;
  line-height: 40px;
  padding: 0 10px;

  &:hover {
    cursor: pointer;
  }
}

.dark .create-topic{
  background-color: #232323;
}
</style>
