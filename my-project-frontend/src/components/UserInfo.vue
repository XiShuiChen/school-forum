<script setup>
import {useStore} from "@/store";
import {isRoleAdmin, logout} from "@/net";
import router from "@/router";
import {computed} from "vue";
import {useRoute} from "vue-router";
import {Right} from "@element-plus/icons-vue";

const store = useStore();
const route = useRoute()

const isAdminPage = computed(() => route.fullPath.startsWith("/admin"))

function userLogout() {
  logout(() => router.push("/"))
}
</script>

<template>
  <div class="user-info">
    <template v-if="isRoleAdmin()">
      <el-button type="primary"
                 size="small"
                 v-if="isAdminPage"
                 @click="router.push('/index')">
        回到用户端
        <el-icon style="margin-left: 5px">
          <Right/>
        </el-icon>
      </el-button>
      <el-button type="primary"
                 size="small"
                 v-else
                 @click="router.push('/admin')">
        前往管理端
        <el-icon style="margin-left: 5px">
          <Right/>
        </el-icon>
      </el-button>
    </template>

    <slot/>

    <div class="profile">
      <div>{{ store.user.username }}</div>
      <div>{{ store.user.email }}</div>
    </div>

    <el-dropdown>
      <el-avatar :src="store.avatarUrl"/>
      <template #dropdown>
        <el-dropdown-item @click="userLogout">
          <el-icon style="height: 30px">
            <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M23.9917 6H6V42H24" stroke="#26272a" stroke-width="3" stroke-linecap="round"
                    stroke-linejoin="round"/>
              <path d="M33 33L42 24L33 15" stroke="#26272a" stroke-width="3" stroke-linecap="round"
                    stroke-linejoin="round"/>
              <path d="M16 23.9917H42" stroke="#26272a" stroke-width="3" stroke-linecap="round"
                    stroke-linejoin="round"/>
            </svg>
          </el-icon>
          退出登录
        </el-dropdown-item>
      </template>
    </el-dropdown>
  </div>
</template>

<style scoped>
.user-info {
  width: 320px;
  display: flex;
  gap: 20px;
  justify-content: flex-end;
  align-items: center;

  .el-avatar:hover {
    cursor: pointer;
  }

  .profile {
    text-align: right;

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
</style>
