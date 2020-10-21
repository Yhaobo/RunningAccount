<template>
  <el-upload style="width: max-content;margin: 5px auto"
             class="upload-demo"
             ref="upload"
             :action="url"
             :on-success="handleSuccess"
             :on-error="handleError"
             :on-exceed="handleExceed"
             :before-upload="beforeUpload"
             :auto-upload="true"
             :list-type="listType"
             :with-credentials="true"
             :accept="accept"
             :multiple="multiple"
             :limit="limit"
  >
    <el-button slot="trigger" size="small" type="success">点击上传</el-button>
    <div slot="tip" class="el-upload__tip" v-text="tipText"></div>
  </el-upload>
</template>

<script>
export default {
  props: {
    multiple: {type: Boolean, default: true},
    url: String,
    listType: {type: String, default: 'text'}, // text/picture/picture-card
    accept: String,  // ".jpg,.jpeg,.png,.gif,.bmp,.pdf,.JPG,.JPEG,.PBG,.GIF,.BMP,.PDF"
    limit: Number, //最大上传文件数
    limitSize: {type: Number, default: 512}, //文件大小限制 单位为KB
    tipText: {type: String}
  },
  data() {
    return {
      successUploadNum: 0, //上传成功的文件个数
    }
  },
  created() {
    // if (!this.tipText) {
    //   this.tipText=`上传文件不得超过 ${this.limitSize}KB`
    // }
  },
  methods: {
    reset() {
      this.successUploadNum = 0
      this.$refs.upload.clearFiles();
    },
    handleSuccess(response, file, fileList) {
      if (response.success) {
        this.successUploadNum++;
        this.$notify.success({
          title: '提示',
          message: `<${file.name}>上传成功`,
          showClose: true
        });
      } else if (response.message) {
        fileList.splice(fileList.indexOf(file), 1);
        this.$notify.error({
          message: response.message,
          title: '提示',
          showClose: true,
          duration: 5 * 1000
        });
      } else {
        fileList.splice(fileList.indexOf(file), 1);
        this.$notify.error({
          message: '请刷新页面后重试',
          title: '提示',
          showClose: true,
          duration: 5 * 1000
        });
      }
    },
    handleError(err, file) {
      this.$notify.warning({
        title: '提示',
        message: `<${file.name}>上传失败`,
        duration: 5 * 1000,
        showClose: true
      });
    },
    handleExceed() {
      this.$message.info({message: `当前限制一次只能上传 ${this.limit} 个文件`, showClose: true})
    },
    beforeUpload(file) {
      if (file.size / 1024 > this.limitSize) {
        this.$notify.warning({
          message: `<${file.name}> 大小超过 ${this.limitSize}KB!`,
          title: '提示',
          showClose: true,
          duration: 5 * 1000
        })
        return false
      }
    }
  }
}
</script>
