package swu.xl.trafficsystem.tencent

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import java.util.*


object TencentCOS : ViewModel() {
    private const val secretId = "AKIDNfjaHj0vkHivB9CQq35mFUa3GE0Xppa6"
    private const val secretKey = "WkkbXOFrLab3kETdfYEfsz59CwXwAkFZ"

    private fun getCosService(context: Context): CosXmlService {
        val region = "ap-guangzhou"
        val serviceConfig = CosXmlServiceConfig.Builder()
            .setRegion(region)
            .isHttps(true)
            .builder()
       return CosXmlService(context, serviceConfig, ShortTimeCredentialProvider(secretId, secretKey, 300))
    }

    fun upload(context: Context, path: String, block: (String) -> Unit) {
        val bucket = "android-project-1300729795"
        val uploadId: String? = null
        val uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase()
        val cosPath = "secondhouse/$uuid"
        val transferConfig = TransferConfig.Builder().build()
        val transferManager = TransferManager(getCosService(context), transferConfig)

        val cosxmlUploadTask = transferManager.upload(bucket, cosPath, path, uploadId)

        cosxmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {
            override fun onSuccess(request: CosXmlRequest, result: CosXmlResult) {
                block(result.accessUrl)
            }

            override fun onFail(
                request: CosXmlRequest,
                clientException: CosXmlClientException,
                serviceException: CosXmlServiceException
            ) {
                if (clientException != null) {
                    clientException.printStackTrace()
                } else {
                    serviceException.printStackTrace()
                }
            }
        })
    }
}