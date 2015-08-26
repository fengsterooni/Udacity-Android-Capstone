package org.csix.android;

import android.test.AndroidTestCase;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;

import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.Event;
import org.csix.backend.myApi.model.Group;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestAppEngine extends AndroidTestCase {

    public static final String LOG_TAG = TestAppEngine.class.getSimpleName();

    private static MyApi myApiService = null;

    public TestAppEngine() {

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null)
                // options for running against local devappserver
                // - 10.0.2.2 is localhost's IP address in Android emulator
                // - turn off compression when running against local devappserver
                //.setRootUrl("http://10.0.2.2:8080/_ah/api/")

                // Genymotion uses 10.0.3.2
                .setRootUrl("http://10.0.3.2:8080/_ah/api/")
                .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                    @Override
                    public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                        abstractGoogleClientRequest.setDisableGZipContent(true);
                    }
                });
        // end options for devappserver

        myApiService = builder.build();
    }

    public void testInitialization() throws Throwable {
        initializeEvents();
        initializeGroups();
    }

    public void initializeEvents() throws Throwable {
        if (myApiService != null) {
            myApiService.removeAllEvents().execute();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Event event = new Event();
            Date date = format.parse("2015-08-27");
            event.setDate(new DateTime(date));
            event.setSpeaker("Jenny Dunham");
            event.setTopic("Tapping into the Hidden Job Market");
            event.setDesc("You’ve applied to every job order on line and posted your resume on every job board, but you’re still not getting in the door. What’s next? Many available jobs are unadvertised. Learn how to get in front of hiring managers  without  standing  in  line!  Use  sales  tricks  and  tips  to  warm up  a  cold  call  and  get  past  the gatekeeper to generate informational meetings. Differentiate yourself from the rest of the crowd by doing what other candidates are unwilling to do and afraid to try.\n" +
                    "\n" +
                    "Jenny Dunham is a Personal Career Coach counseling clients throughout various stages of their career transition.  She is a recognized facilitator and speaker who enjoys uncovering natural talent, pinpointing goals and turning dreams into reality.\n" +
                    "\n" +
                    "Jenny’s background with sales, recruiting, finance and technology provides a unique perspective with a focus on creating a personal brand to “sell” your background into companies.  Her workshops including “Tapping into the Hidden Job Market”, Laser-Focused Search” and “Building Instant Rapport” encourage clients to step out of their comfort zone to land their ideal job.\n" +
                    "\n" +
                    "Jenny brings over 15 years of recruiting and business development experience primarily marketing financial consulting services as the Director of Business Development in a boutique staffing firm in the Silicon Valley.  She returned to coaching after several years marketing financial and “big data” software to technology companies as part of a late stage startup.");
            event.setImage("http://csix.org/wp-content/uploads/2015/08/JennyDunham.jpg");
            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-09-03");
            event.setDate(new DateTime(date));
            event.setSpeaker("Katie Fagan");
            event.setTopic("Franchising 101: Is Franchising Right For You?");
            event.setDesc("In corporate life, career advancement and dependability always depends on others, and almost never depends on performance. With a franchise business you could control your own destiny, and create a future where you directly reap the rewards of your efforts. This class provides an overview of the options available to the business buyer, franchising facts, selecting the right franchise and keys to success in operating a new franchise. Topics include: franchising facts–debunking myths, buying existing businesses, selecting the right franchise/business, and keys to success in operating a new franchise.\n" +
                    "\n" +
                    "Katie has over 15 years of experience in the world of small business. In her corporate career she worked as the Vice President of Small Business Funding, focusing primarily on funding start-up franchises and businesses. During her time there, she gained extensive knowledge on the ins-and-outs of franchising from the back end forward. Katie has helped over 600 small businesses open their doors, many of which were franchises, and has facilitated hundreds of SBA loans. Katie also worked in business brokerage, as a co-founder of a boutique brokerage firm focusing on the resale of restaurants, nightclubs and franchises. Currently, Katie teaches Entrepreneurship at universities across the Bay Area, volunteers with the San Jose Chapter of SCORE, as well as working with other local government and non-profit agencies educating people on the ins-and-outs of small business ownership.\n" +
                    "Katie holds an MBA in Entrepreneurship and Finance and a PhD in Mass Communications.\n" +
                    "\n");
            event.setImage("http://csix.org/wp-content/uploads/2015/08/kfagan.jpg");
            myApiService.insertEvent(event).execute();

        }
    }

    public void initializeGroups() throws Throwable {
        if (myApiService != null) {
            myApiService.removeAllGroups().execute();

            Group group = new Group();
            group.setName("CSix Connect, Burlingame");
            group.setAddress("1500 Easton Drive, Burlingame, CA 94010");
            group.setLocation("First Presbyterian Church");
            group.setTime("First Tuesdays every month, 6:00 pm - 8:30 pm");
            group.setDesc("CSix Peninsula Chapter");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Eco Green Group (EGG)");
            group.setAddress("20390 Park Place, Saratoga, CA");
            group.setLocation("Richards Hall, Saratoga Federated Church");
            group.setTime("Thursday, 8:00 am – 9:30 am");
            group.setDesc("EcoGreen Group creates professional and entrepreneurial opportunities in sustainability and clean technologies through education, professional development, industry collaboration, and networking.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Manufacturing/Operations SIG");
            group.setAddress("1000 S. Bascom Avenue, San Jose, CA 95128");
            group.setLocation("Community Room – 2nd Floor, Bascom Library & Community Center");
            group.setTime("Every 1st and 3rd Wednesday of each month, 9:00 am – 11:00 am");
            group.setDesc("Networking, job leads, and information focused on the corporate functions of manufacturing and more general operations including Facilities, Logistics, Supply Chain, Procurement, Materials, Manufacturing & Test Engineering and Quality Engineering.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Finance SIG");
            group.setAddress("118 East El Camino Real, Sunnyvale, CA 94087");
            group.setLocation("Panera Bread, Sunnyvale");
            group.setTime("Every 2nd and 4th Wednesday of each month, 7:30 am – 9:00 pm");
            group.setDesc("Our mission is to bring together finance professionals in the South Bay Area so that we may explore the uniqueness of working in this field and how that pertains to job searching, professional development, and being successful on the job.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Job Search Empowerment on the Peninsula (JSEP)");
            group.setAddress("399 Marine Parkway, Redwood City, CA 94065");
            group.setLocation("Meeting Room #B, Redwood Shores Library");
                    group.setTime("Monday (Except holidays), 5:30 pm – 6:30 pm");
            group.setDesc("Help one another with resumes\n" +
                    "Practice interview skills\n" +
                    "Develop a concise elevator pitch\n" +
                    "Learn proper dress for interviewing in today’s market\n" +
                    "Develop networking skills\n" +
                    "Strive for accountability");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Semiconductor SIG (SemiSIG)");
            group.setAddress("1000 S. Bascom Avenue, San Jose, CA 95128");
            group.setLocation("Community Room – 2nd Floor, Bascom Library & Community Center");
            group.setTime("Every 1st and 3rd Wednesday of each month, 9:00 am – 11:00 am");
            group.setDesc("SemiSIG is a Special Interest Group for professionals in the semiconductor industry, and in the related industries of flat panel display, magnetic, optical and thin film coating. Our network was formed to improve our members’ knowledge of these industries and to share career change opportunities.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Morning Wildcat Loop Trail Hike");
            group.setAddress("22500 Cristo Rey Drive, Los Altos, CA");
            group.setLocation("Rancho San Antonio");
            group.setTime("Every Tuesday, 6:30 am - 8:00 am");
            group.setDesc("A fun group of people who meet every Tuesday morning at 6:30 a.m. at Rancho San Antonio in Cupertino (off Hwy 280 & 85) for an early morning networking hike. It’s a terrific way to keep fit and meet interesting people at the same time. This is a particularly good group for people who may be in career transition mode because of the group’s philosophy of mutual support, the power of networking and a group hike! We are finished by 8:00 a.m. so you can get on with your day and feel fantastic because you have had a great workout. We hope you can join us soon!");
            myApiService.insertGroup(group).execute();
        }
    }
}
