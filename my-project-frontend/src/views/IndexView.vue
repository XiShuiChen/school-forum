<script setup>
import {get, logout} from '@/net'
import router from "@/router";
import {useStore} from "@/store";
import {reactive, ref} from "vue";
import {
  Bell,
  Calendar,
  ChatDotSquare, Check, Collection, DataLine,
  Document,
  Location, Lock, Message,
  Money, Monitor,
  Notification, Operation,
  Position,
  School, Search, Setting,
  Umbrella, User
} from "@element-plus/icons-vue";
import LightCard from "@/components/LightCard.vue";
import {createRouter, createWebHistory} from "vue-router";
import UserInfo from "@/components/UserInfo.vue";

const store = useStore()
const loading = ref(true)

const searchInput = reactive({
  type: '1',
  text: ''
})

const notification = ref([])

get('/api/user/info', (data) => {
  store.user = data;
  loading.value = false
})

const loadNotification =
    () => get('/api/notification/list', data => notification.value = data)
loadNotification()

function confirmNotification(id, url) {
  get(`api/notification/delete?id=${id}`, () => {
    loadNotification()
    window.open(url)
  })
}

function deleteAllNotification() {
  get('/api/notification/delete-all', loadNotification)
}

const jumpToUrl = (url) => {
  window.open(url, '_blank');
};
</script>


<template>
  <div class="main-content" v-loading="loading" element-loading-text="正在加载，请稍后...">
    <el-container style="height: 100%" v-if="!loading">
      <el-header class="main-content-header">
        <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
        <div style="flex: 1; padding:0 20px; text-align: center">
          <el-input v-model="searchInput.text" style="width: 100%; max-width: 500px" placeholder="搜索论坛相关内容...">
            <template #prefix>
              <el-icon>
                <Search/>
              </el-icon>
            </template>

            <template #append>
              <el-select style="width: 100px" v-model="searchInput.type">
                <el-option value="1" label="帖子广场"/>
              </el-select>
            </template>
          </el-input>
        </div>

        <user-info>
          <el-popover placement="bottom" :width="350" trigger="click">
            <template #reference>
              <el-badge style="margin:6px 15px 0 0;" is-dot :hidden="!notification.length">
                <div class="notification">
                  <el-icon><Bell/></el-icon>
                  <div style="font-size: 10px">消息</div>
                </div>
              </el-badge>
            </template>

            <el-empty :image-size="80" description="暂时没有未读消息..." v-if="!notification.length"/>
            <el-scrollbar :max-height="500" v-else>
              <light-card v-for="item in notification" class="notification-item"
                          @click="confirmNotification(item.id, item.url)">
                <div>
                  <el-tag size="small" :type="item.type">新消息</el-tag>&nbsp;
                  <span style="font-weight: bold">{{ item.title }}</span>
                </div>
                <el-divider style="margin: 7px 0 3px 0"/>
                <div style="font-size: 13px; color: #5d5959">
                  {{ item.content }}
                </div>
              </light-card>
            </el-scrollbar>

            <div style="margin-top: 10px">
              <el-button @mousedown="e => e.preventDefault()" size="small" type="success" :icon="Check"
                         @click="deleteAllNotification" style="width: 100%" plain>清除全部未读消息</el-button>
            </div>
          </el-popover>
        </user-info>

      </el-header>
      <el-container>
        <el-aside width="230px">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <el-menu
                router
                :default-active="$route.path"
                :default-openeds="['1', '2', '3']"
                style="min-height: calc(100vh - 55px)"
            >
              <el-sub-menu index="1">
                <template #title>
                  <el-icon>
                    <Location/>
                  </el-icon>
                  <span><b>校园论坛</b></span>
                </template>

                <el-menu-item index="/index">
                  <template #title>
                    <el-icon>
                      <ChatDotSquare/>
                    </el-icon>
                    帖子广场
                  </template>
                </el-menu-item>
              </el-sub-menu>

              <el-sub-menu index="2">
                <template #title>
                  <el-icon>
                    <Position/>
                  </el-icon>
                  <span><b>学校相关</b></span>
                </template>

                <el-menu-item @click="jumpToUrl('http://i.zut.edu.cn/new/index.html')">
                  <template #title>
                    <el-icon>
                      <Monitor/>
                    </el-icon>
                    网络服务大厅
                  </template>
                </el-menu-item>

                <el-menu-item @click="jumpToUrl('https://jwc.zut.edu.cn/fwxx/xl.htm')">
                  <template #title>
                    <el-icon>
                      <Calendar/>
                    </el-icon>
                    学年校历
                  </template>
                </el-menu-item>

                <el-menu-item @click="jumpToUrl('https://jwc.zut.edu.cn/index.htm')">
                  <template #title>
                    <el-icon>
                      <Document/>
                    </el-icon>
                    教务通知
                  </template>
                </el-menu-item>
              </el-sub-menu>

              <el-sub-menu index="3">
                <template #title>
                  <el-icon>
                    <Setting/>
                  </el-icon>
                  <span><b>个人设置</b></span>
                </template>

                <el-menu-item index="/index/user-setting">
                  <template #title>
                    <el-icon>
                      <User/>
                    </el-icon>
                    个人信息设置
                  </template>
                </el-menu-item>

                <el-menu-item index="/index/privacy-setting">
                  <template #title>
                    <el-icon>
                      <Lock/>
                    </el-icon>
                    账号安全设置
                  </template>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-scrollbar>
        </el-aside>


        <el-main class="main-content-page">
          <el-scrollbar style="height: calc(100vh - 55px)">
            <router-view v-slot="{ Component }">
              <transition name="el-fade-in-linear" mode="out-in">
                <component :is="Component" style="height: 100%"/>
              </transition>
            </router-view>
          </el-scrollbar>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>


<style lang="less" scoped>
.notification-item {
  transition: .3s;
  &:hover {
    cursor: pointer;
    opacity: 0.7;
  }
}

.notification {
  font-size: 22px;
  line-height: 14px;
  text-align: center;
  transition: color .3s;

  &:hover {
    color: grey;
    cursor: pointer;
  }
}

.main-content {
  height: 100vh;
  width: 100vw;
}

.main-content-header {
  border-bottom: solid 1px var(--el-border-color);
  height: 55px;
  display: flex;
  align-items: center;
  box-sizing: border-box;

  .logo {
    height: 32px;
  }

  .user-info {
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .el-avatar:hover {
      cursor: pointer;
    }

    .profile {
      text-align: right;
      margin-right: 20px;

      :first-child {
        font-size: 20px;
        font-weight: bold;
        line-height: 20px;
      }

      :last-child {
        font-size: 12px;
        color: gray;
        margin-top: 4px;
      }
    }
  }
}

.main-content-page {
  padding: 0;
  background-color: #f7f8fa;
}

.dark .main-content-page{
  background-color: #212225;
}

:deep(.el-avatar) {
  outline: none
}
</style>
