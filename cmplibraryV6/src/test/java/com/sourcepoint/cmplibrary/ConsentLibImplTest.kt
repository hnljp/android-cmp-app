package com.sourcepoint.cmplibrary

import android.content.Context
import com.sourcepoint.cmplibrary.creation.createClientInfo
import com.sourcepoint.cmplibrary.data.Service
import com.sourcepoint.cmplibrary.data.local.DataStorage
import com.sourcepoint.cmplibrary.data.network.NetworkClient
import com.sourcepoint.cmplibrary.data.network.converter.JsonConverter
import com.sourcepoint.cmplibrary.data.network.util.HttpUrlManager
import com.sourcepoint.cmplibrary.data.network.util.ResponseManager
import com.sourcepoint.cmplibrary.model.Campaign
import com.sourcepoint.cmplibrary.util.ConnectionManager
import com.sourcepoint.cmplibrary.util.ExecutorManager
import com.sourcepoint.cmplibrary.util.ViewsManager
import com.sourcepoint.gdpr_cmplibrary.PrivacyManagerTab
import com.sourcepoint.gdpr_cmplibrary.exception.ErrorMessageManager
import com.sourcepoint.gdpr_cmplibrary.exception.Logger
import com.sourcepoint.gdpr_cmplibrary.exception.MissingClientException
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ConsentLibImplTest {

    var campaign = Campaign(
        22,
        7639,
        "tcfv2.mobile.webview",
        "122058"
    )

    val client = createClientInfo()

    @MockK
    private lateinit var appCtx: Context

    @MockK
    private lateinit var errorManager: ErrorMessageManager

    @MockK
    private lateinit var logger: Logger

    @MockK
    private lateinit var jsonConverter: JsonConverter

    @MockK
    private lateinit var connManager: ConnectionManager

    @MockK
    private lateinit var responseManager: ResponseManager

    @MockK
    private lateinit var networkClient: NetworkClient

    @MockK
    private lateinit var dataStorage: DataStorage

    @MockK
    private lateinit var viewManager: ViewsManager

    @MockK
    private lateinit var execManager: ExecutorManager

    @MockK
    private lateinit var spClient: SpClient

    @MockK
    private lateinit var urlManager: HttpUrlManager

    @MockK
    private lateinit var service: Service

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = true)
    }

    @Test(expected = MissingClientException::class)
    fun `CALLING loadMessage() with a null SpGDPRClient THROWS a MissingClientException`() {
        val sut = ConsentLibImpl(urlManager, campaign, PrivacyManagerTab.FEATURES, appCtx, logger, jsonConverter, connManager, service, viewManager, execManager)
        sut.loadMessage()
        verify(exactly = 1) { service.getMessage(any(), any(), any()) }
    }

    @Test
    fun `CALLING loadMessage() verify that getMessage is called exactly 1 time`() {
        val sut = ConsentLibImpl(urlManager, campaign, PrivacyManagerTab.FEATURES, appCtx, logger, jsonConverter, connManager, service, viewManager, execManager)
        sut.spClient = spClient
        sut.loadMessage()
        verify(exactly = 1) { service.getMessage(any(), any(), any()) }
    }
}