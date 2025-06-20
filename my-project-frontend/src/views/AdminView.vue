<script setup>

import {
  Calendar,
  ChatDotSquare,
  Document,
  Location,
  Lock,
  Monitor,
  Position,
  Setting,
  User
} from "@element-plus/icons-vue";

const adminMenu = [
  {
    title:'校园论坛管理', icon: Location, sub:[
      {title: '用户管理', icon: User},
      {title: '帖子广场管理', icon: ChatDotSquare},
    ]
  }, {
    title:'探索与发现管理', icon: Location, sub:[
      {title: '学年校历管理', icon: Calendar},
      {title: '教务通知管理', icon: Document},
    ]
  }
]
</script>

<template>
  <div class="admin-content">
    <el-container style="height: 100%">
      <el-aside width="230px" class="admin-content-aside">
        <div class="logo-box">
          <el-image class="logo" src="https://element-plus.org/images/element-plus-logo.svg"/>
        </div>

        <el-scrollbar style="height: calc(100vh - 57px)">
          <el-menu
              router
              :default-active="$route.path"
              :default-openeds="['1', '2']"
              style="min-height: calc(100vh - 57px); border: none"
          >
            <el-sub-menu :index="(index + 1).toString()" v-for="(menu, index) in adminMenu">
              <template #title>
                <el-icon>
                  <component :is="menu.icon"/>
                </el-icon>
                <span><b>{{ menu.title }}</b></span>
              </template>

              <el-menu-item :index="subMenu.index" v-for="subMenu in menu.sub">
                <template #title>
                  <el-icon>
                    <component :is="subMenu.icon"/>
                  </el-icon>
                  {{ subMenu.title }}
                </template>
              </el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-scrollbar>
      </el-aside>


      <el-container>
        <el-header>Header</el-header>
        <el-main>Main</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.admin-content {
  height: 100vh;
  width: 100vw;

  .admin-content-aside {
    border-right: solid 1px var(--el-border-color);

    .logo-box {
      text-align: center;
      padding: 15px 0 10px;
      height: 32px;

      .logo {
        height: 32px;
    }
    }
  }
}
</style>
