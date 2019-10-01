package com.spectraparent.WebAPI;

/**
 * Created by VikasNokhwal on 03-10-2017.
 */

public class WebApi {
    public static final String BaseUrl = "https://spectradriveapp.com/";


    public static final String UpdateLocationUrl = BaseUrl+"Users/UpdateLocation";
    public static final String SaveQueryUrl = BaseUrl+"Users/SaveQuery";
    public static final String GetAllFaqsUrl = "https://spectradriveapp.com/api/FAQs/GetAllFaqs";
    public static final String CreateDummyRideUrl = BaseUrl+"Users/CreateDummyRide";
    public static final String CancelRide = BaseUrl+"Users/CancellationRide";

    public static final String SignInUrl = BaseUrl+"Users/LoginOrRegister";
    public static final String GetMyRidesUrl = BaseUrl+"Users/GetUserRides";
    public static final String RemoveChild = BaseUrl+"Users/RemoveChild";
    public static final String ReSignInUrl = "https://spectradriveapp.com/Login/ReSignIn?isDormWith=true";
    public static final String ForgotPasswordUrl = "https://spectradriveapp.com/Login/DormWithForgetPassword";
    public static final String OAuthSignInUrl = "https://spectradriveapp.com/Login/OAuthLogin?isDormWith=true";
    public static final String SignUpUrl = "https://spectradriveapp.com/Login/SignUp?isDormWith=true";
    public static final String GetSchoolsUrl = "https://spectradriveapp.com/Common/GetSchoolList";
    public static final String GetSchoolDormsUrl = "https://spectradriveapp.com/Common/GetSchoolDorms";
    public static final String GetSchoolMajorsUrl = "https://spectradriveapp.com/Common/GetSchoolMajors";
    public static final String UpdateAccountEducationUrl = "https://spectradriveapp.com/ProfessionalProfile/UpdateAccountEducation";
    public static final String AddSchoolDormUrl = "https://spectradriveapp.com/common/AddSchoolDorm";
    public static final String GetInstagramMediaUrl = "https://spectradriveapp.com/Profile/GetInstagramMedia";
    public static final String UpdateProfileUrl = BaseUrl+"Users/UpdateProfile";
    public static final String AddChildUrl = BaseUrl+"Users/AddChild";
        public static final String AddTrustedPersonUrl = BaseUrl+"Users/AddTrustedPerson";
    public static final String LinkInstagramUrl = "https://spectradriveapp.com/Login/LinkInstagram";
    public static final String GetSchoolmatesUrl = "https://spectradriveapp.com/Common/GetSchoolmates";
    public static final String GetFriendListUrl = "https://spectradriveapp.com/Profile/GetFriends";
    public static final String GetPeopleYouMayKnowUrl = "https://spectradriveapp.com/Friendship/getPeopleYouMayKnow";
    public static final String SendConnectionRequestUrl = "https://spectradriveapp.com/Profile/SendFriendRequest";
    public static final String GetFriendsByPatternUrl = "https://spectradriveapp.com/Profile/FindFriendBasedOnPattern";

    public static final String GetFriendRequestsUrl = "https://spectradriveapp.com/Notification/GetFriendRequests";
    public static final String ApproveConnectionRequestUrl = "https://spectradriveapp.com/Notification/ConfirmFriend";

    public static final String GetMyCoursesUrl = "https://spectradriveapp.com/StudentCourse/GetMyCoursesWithNotesInfo";
    public static final String GetCommentsBySchoolCourseIdUrl = "https://spectradriveapp.com/Comments/GetCommentsBySchoolCourseId";
    public static final String GetClassmatesByStudentCourseIdUrl = "https://spectradriveapp.com/StudentCourse/GetClassmatesByStudentCourseId";
    public static final String GetSchoolmateByIdUrl = "https://spectradriveapp.com/Common/GetSchoolmateById";

    public static final String GetDepartmentsUrl = "https://spectradriveapp.com/Common/GetDepartments";
    public static final String GetCoursesBySchoolDepartmentUrl = "https://spectradriveapp.com/Common/GetCoursesBySchoolDepartment";
    public static final String GetInstructorsBySchoolDepartmentAndCourseNumberUrl = "https://spectradriveapp.com/Common/GetInstructorsBySchoolDepartmentAndCourseNumber";
    public static final String GetSchoolCourseSectionsUrl = "https://spectradriveapp.com/StudentCourse/GetSchoolCourseSections";
    public static final String AddStudentCourseUrl = "https://spectradriveapp.com/StudentCourse/AddMyCourses";

    public static final String SavePersonalInfomationUrl = "https://spectradriveapp.com/Setting/SavePersonalInfomation";

    public static final String GetChatsByUserId = "https://spectradriveapp.com/Chat/GetChatsByUserId?start=0&length=100";
    public static final String GetChatMessagesByChatIdUrl = "https://spectradriveapp.com/Chat/GetChatMessagesByChatId";
    public static final String GetSchoolGroupChatsUrl = "https://spectradriveapp.com/Chat/GetSchoolsGroupsChatsByUserId";
    public static final String GetInterestGroupChatsUrl = "https://spectradriveapp.com/Chat/GetInterestsGroupsChatsByUserId";


    public static final String AddMessageToPrivateChatUrl = "https://spectradriveapp.com/Chat/AddMessageToPrivateChat";
    public static final String UploadChatAttachmentUrl = "https://spectradriveapp.com/Azure/AddChatAttachment";
    public static final String AddAndroidFCMTokenUrl = "https://spectradriveapp.com/Notification/AddAndroidFCMToken";
    public static final String RemoveAndroidFCMTokenUrl = "https://spectradriveapp.com/Notification/RemoveAndroidFCMToken";

    public static final String CreateGroupChatUrl = "https://spectradriveapp.com/Chat/CreateGroupChat";
    public static final String GetGroupChatParticipentsUrl = "https://spectradriveapp.com/Chat/getgroupchatParticipents";
    public static final String MarkChatReadUrl = "https://spectradriveapp.com/Chat/MarkChatRead";
    public static final String GetChatByIdUrl = "https://spectradriveapp.com/Chat/GetChatById";
    public static final String CreateChatsWithOutMessageUrl = "https://spectradriveapp.com/Chat/CreateChat";


    public static final String GetStickersUrl = "https://spectradriveapp.com/Chat/GetStickers";
    public static String UpdateLastSeenUrl = "https://spectradriveapp.com/Login/UpdateLastSeen";


    public static final String GetMyJobsUrl = "https://spectradriveapp.com/Jobs/GetMyJobs?start=0&length=100";
    public static final String GetJobsUrl = "https://spectradriveapp.com/Jobs/GetJobs?start=0&length=100";

    //
    public static final String BlockUserUrl = "https://spectradriveapp.com/Login/BlockUser";
    public static final String ReportUrl = "https://spectradriveapp.com/Login/Report";
    public static final String CaptureDeepLinkUrl = "https://spectradriveapp.com/Login/StoreDLink";
    public static final String JoinGroupUrl = "https://spectradriveapp.com/Login/JoinGroup";
    public static final String GetGroupShareLinkUrl = "https://spectradriveapp.com/Chat/GetGroupShareLink";

//
}
