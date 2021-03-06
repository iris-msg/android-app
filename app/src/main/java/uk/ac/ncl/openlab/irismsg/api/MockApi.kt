package uk.ac.ncl.openlab.irismsg.api

import retrofit2.mock.Calls
import uk.ac.ncl.openlab.irismsg.common.ApiCall
import uk.ac.ncl.openlab.irismsg.common.MemberRole
import uk.ac.ncl.openlab.irismsg.model.*

/**
 * A fake implementation of the Iris Msg api using locally generated entities
 */
class MockIrisMsgService : IrisMsgService {
    
    private val generator = EntityGenerator()
    private val fixedMessages = listOf(
        generator.makePendingMessage("Hello there"),
        generator.makePendingMessage("General Kenobi")
    )
    
    private fun <T> success (data: T? = null) : ApiCall<T> {
        return MockTemporalCall(ApiResponse.success(data))
    }
    
    override fun ping() : ApiCall<String> {
        return success("ok")
    }

    override fun getAppVersion(): ApiCall<AppVersion> {
        return success(generator.makeAppVersion())
    }
    
    override fun getSelf(): ApiCall<UserEntity> {
        return success(
            generator.makeUser(UserGen.CURRENT)
        )
    }
    override fun requestLogin(body: RequestLoginRequest): ApiCall<Any> {
        return Calls.response(ApiResponse.create(body.phoneNumber != "fail"))
    }
    override fun checkLogin(body: CheckLoginRequest): ApiCall<UserAuthEntity> {
        return Calls.response(
            if (body.code >= 0) ApiResponse.success(generator.makeUserAuth())
            else ApiResponse.fail()
        )
    }
    override fun updateFcm(body: UpdateFcmRequest): ApiCall<Any> {
        return Calls.response(ApiResponse.success())
    }
    
    
    override fun listOrganisations(): ApiCall<List<OrganisationEntity>> {
        return success(listOf(
            generator.makeOrganisation(OrganisationGen.COORDINATOR),
            generator.makeOrganisation(OrganisationGen.COORDINATOR),
            generator.makeOrganisation(OrganisationGen.DONOR),
            generator.makeOrganisation(OrganisationGen.DONOR),
            generator.makeOrganisation(OrganisationGen.DONOR)
        ))
    }
    override fun showOrganisation(id: String): ApiCall<OrganisationEntity> {
        return success(
            generator.makeOrganisation(OrganisationGen.COORDINATOR)
        )
    }
    override fun createOrganisation(body: CreateOrganisationRequest): ApiCall<OrganisationEntity> {
        val org = generator.makeOrganisation(OrganisationGen.COORDINATOR)
        org.name = body.name
        org.info = body.info
        return success(org)
    }
    override fun destroyOrganisation(id: String): ApiCall<Any> {
        return success()
    }
    
    override fun listOrganisationMembers(id: String) : ApiCall<List<OrganisationMemberEntity>> {
        return success(listOf(
            generator.makeOrganisationMember(MemberRole.COORDINATOR, UserGen.CURRENT),
            generator.makeOrganisationMember(MemberRole.DONOR, UserGen.CURRENT),
            generator.makeOrganisationMember(MemberRole.SUBSCRIBER, UserGen.VERIFIED),
            generator.makeOrganisationMember(MemberRole.SUBSCRIBER, UserGen.VERIFIED)
        ))
    }
    
    
    override fun createMember(organisationId: String, body: CreateMemberRequest) : ApiCall<MemberEntity> {
        return success(generator.makeMember(body.role, UserGen.VERIFIED))
    }
    override fun destroyMember(memberId: String, organisationId: String): ApiCall<Any> {
        return success()
    }
    override fun acceptInvite(token: String): ApiCall<UserAuthEntity> {
        return success(generator.makeUserAuth())
    }
    
    override fun showInvite(token : String) : ApiCall<MemberInviteEntity> {
        return success(generator.makeMemberInvite())
    }
    
    
    override fun createMessage(body: CreateMessageRequest): ApiCall<MessageEntity> {
        return success(generator.makeMessage(body.orgId))
    }
    override fun listPendingMessages() : ApiCall<List<PendingMessageEntity>> {
        // Note: Uses 2 static messages and generates the last one each request
        val messages = ArrayList(fixedMessages)
        messages.add(generator.makePendingMessage("You are a bold one"))
        return success(messages)
    }
    override fun updateMessageAttempts(body: UpdateMessageAttemptsRequest): ApiCall<Any> {
        return success()
    }
    
}