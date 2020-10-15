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
  >
    <el-button slot="trigger" size="small" type="success">点击上传</el-button>
    <div slot="tip" class="el-upload__tip">只能上传图片文件，且不超过 500KB</div>
  </el-upload>
</template>

<script>
export default {
  props: {
    multiple: {type: Boolean, default: true},
    url: String,
    listType: {type: String, default: 'text'}, // text/picture/picture-card
    accept: String,  // ".jpg,.jpeg,.png,.gif,.bmp,.pdf,.JPG,.JPEG,.PBG,.GIF,.BMP,.PDF"
    limitSize: Number //单位为KB
  },
  data() {
    return {
      successUploadNum: 0, //上传成功的文件个数
    }
  },
  created() {
  },
  methods: {
    reset() {
      this.successUploadNum = 0
      this.$refs.upload.clearFiles();
    },
    handleSuccess(response, file) {
      if (response.success) {
        this.successUploadNum++;
        this.$notify.success({
          title: '提示',
          message: `<${file.name}>上传成功`,
          showClose: true
        });
      } else if (response.message) {
        this.$notify.error({
          message: response.message,
          title: '提示',
          showClose: true,
          duration: 5 * 1000
        });
      } else {
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
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
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
