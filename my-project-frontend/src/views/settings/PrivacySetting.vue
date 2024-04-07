<script setup>

import Card from "@/components/Card.vue";
import {EditPen, Refresh, Setting, Lock} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const passwordForm = reactive({
  password: '',
  new_password: '',
  new_password_repeat: '',
})

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== passwordForm.new_password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  password: [
    {required: true, message: '请输入原来的密码', trigger: 'blur'},
    {min: 6, max: 20, message: '密码的长度必须在6-20个字符之间', trigger: ['blur']}
  ],
  new_password: [
    {required: true, message: '请输入新的密码', trigger: 'blur'},
    {min: 6, max: 20, message: '密码的长度必须在6-20个字符之间', trigger: ['blur']}
  ],
  new_password_repeat: [
    {required: true, message: '请重复新的密码', trigger: 'blur'},
    {validator: validatePassword, trigger: ['blur', 'change']},
  ]
}

const passwordFormRef = ref()
const valid = ref(false)
const onValidate = (prop, isValid) => valid.value = isValid

function resetPassword() {
  passwordFormRef.value.validate(valid => {
    if (valid) {
      post('/api/user/change-password', passwordForm, () => {
        ElMessage.success("修改密码成功！")
        passwordFormRef.value.resetFields();
      })
    }
  })
}
</script>

<template>
  <div style="margin:auto; max-width: 600px">
    <div style="margin-top: 20px">
      <card :icon="Setting" title="隐私设置" desc="请在这里设置您的个人隐私">
        <div class="checkbox-list">
          <el-checkbox>公开展示我的手机号</el-checkbox>
          <el-checkbox>公开展示我的电子邮件地址</el-checkbox>
          <el-checkbox>公开展示我的微信号</el-checkbox>
          <el-checkbox>公开展示我的QQ号</el-checkbox>
          <el-checkbox>公开展示我的性别</el-checkbox>
        </div>
      </card>

      <card style="margin: 20px 0" :icon="EditPen" title="修改密码" desc="请在这里修改您的密码">
        <el-form :rules="rules" :model="passwordForm" ref="passwordFormRef" @validate="onValidate"
                 label-width="100" style="margin: 20px">
          <el-form-item label="当前密码" prop="password">
            <el-input type="password" :prefix-icon="Lock" v-model="passwordForm.password"
                      placeholder="当前密码" maxlength="20"/>
          </el-form-item>

          <el-form-item label="新密码" prop="new_password">
            <el-input type="password" :prefix-icon="Lock" v-model="passwordForm.new_password"
                      placeholder="新密码" maxlength="20"/>
          </el-form-item>

          <el-form-item label="重复新密码" prop="new_password_repeat">
            <el-input type="password" :prefix-icon="Lock" v-model="passwordForm.new_password_repeat"
                      placeholder="重复密码" maxlength="20"/>
          </el-form-item>

          <div style="text-align: center">
            <el-button :icon="Refresh" @click="resetPassword" plain type="success">立即重置密码</el-button>
          </div>
        </el-form>
      </card>
    </div>
  </div>
</template>

<style scoped>
.checkbox-list {
  margin: 10px 0 0 10px;
  display: flex;
  flex-direction: column;
}
</style>