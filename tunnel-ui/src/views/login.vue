<template>
  <div class="login">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <div
        style="
          width: 100%;
          text-align: center;
          padding-right: 20px;
          padding-bottom: 10px;
        "
      >
        <img src="../assets/image/login-logo.png" width="120px;" />
      </div>
      <div class="title">智慧隧道综合管控平台</div>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
          <svg-icon
            slot="prefix"
            icon-class="user"
            class="el-input__icon input-icon"
          />
        </el-input>
        <div style="border-bottom: 1px solid #cfcfcf; margin-top: 5px"></div>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon
            slot="prefix"
            icon-class="password"
            class="el-input__icon input-icon"
          />
        </el-input>
        <div style="border-bottom: 1px solid #cfcfcf; margin-top: 5px"></div>
      </el-form-item>
      <Verify
        @success="capctchaCheckSuccess"
        :mode="'pop'"
        :captchaType="'blockPuzzle'"
        :imgSize="{ width: '330px', height: '155px' }"
        ref="verify"
      ></Verify>
      <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 20px 0px;">记住密码</el-checkbox> -->
      <el-form-item style="width: 100%; text-align: center">
        <el-button
          :loading="loading"
          size="medium"
          type="goon"
          style="width: 100%"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right" v-if="register">
          <router-link class="link-type" :to="'/register'"
            >立即注册</router-link
          >
        </div>
      </el-form-item>
    </el-form>

    <!--  底部  -->
    <div class="el-login-footer">
      <!-- <span>Copyright ©hamdell All Rights Reserved.</span> -->
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import Verify from "@/components/Verifition/Verify";
import { getCaptchaOnOff } from "@/api/login";
import { listOrder } from "@/api/payment/order";
import { getUserDeptId } from "@/api/system/user";
import { listTunnels } from "@/api/equipment/tunnel/api.js";
import { getConfigKey } from "@/api/system/config.js";

export default {
  components: { Verify },
  name: "Login",
  data() {
    return {
      title: "", // 系统标题
      cookiePassword: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: "",
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" },
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" },
        ],
      },
      loading: false,
      // 验证码开关
      captchaOnOff: false,
      // 注册开关
      register: false,
      redirect: undefined,

    };
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true,
    },
  },
  created() {
    this.getCookie();
    this.getCaptchaOnOff();
    this.title = systemConfig.title(systemConfig.systemType);
  },
  methods: {
    // 获取验证码是否开启
    getCaptchaOnOff() {
      getCaptchaOnOff().then((response) => {
        this.captchaOnOff = response.captchaOnOff;
      });
    },
    // 验证码
    capctchaCheckSuccess(params) { 
      this.loginForm.code = params.captchaVerification;
      this.loading = true;
      if (this.loginForm.rememberMe) {
        Cookies.set("username", this.loginForm.username, { expires: 30 });
        Cookies.set("password", encrypt(this.loginForm.password), {
          expires: 30,
        });
        Cookies.set("rememberMe", this.loginForm.rememberMe, { expires: 30 });
      } else {
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }
      this.$store
        .dispatch("Login", this.loginForm)
        .then(() => {
          this.$router.push({ path: this.redirect || "/" }).catch(() => {});
        })
        .catch(() => {
          this.loading = false;
        });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get("rememberMe");
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password:
          password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    handleLogin() {
      if (this.captchaOnOff) {
        this.$refs.loginForm.validate((valid) => {
          if (valid) {
            this.$refs.verify.show();
          }
        });
      } else {
        this.$store
          .dispatch("Login", this.loginForm)
          .then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(() => {});
            this.getManageStation();
          })
          .catch(() => {
            this.loading = false;
          });
      }
    },
    getManageStation() {
       getConfigKey("sd.moduleSwitch").then((res) => {
          console.log(res,"管理站01");
          this.$cache.local.set("manageStation",res.msg)
        });
    },
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  position: relative;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 10px auto;
  font-size: 24px;
  text-align: center;
  color: #353f55;
  letter-spacing: 2px;
}

.login-form {
  position: absolute;
  left: 50%;
  top: 20%;
  transform: translateX(-50%);
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 40px 70px 25px 70px;
  // margin-left: 50%;
  .el-input {
    height: 35px;
    input {
      border: 0;
      height: 35px;
    }
  }
  .input-icon {
    height: 35px;
    width: 14px;
    margin-left: 2px;
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 37%;
  height: 35px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 35px;
}

/* 重置按钮 */
.el-button--goon {
  color: #ffffff;
  background-color: #4a5c73;
}

.el-button--goon:hover {
  color: #ffffff;
  background-color: #304156;
}
</style>
