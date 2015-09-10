package org.csix.android;

import android.test.AndroidTestCase;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.util.DateTime;

import org.csix.backend.myApi.MyApi;
import org.csix.backend.myApi.model.About;
import org.csix.backend.myApi.model.Event;
import org.csix.backend.myApi.model.Group;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestAppEngine extends AndroidTestCase {

    public static final String LOG_TAG = TestAppEngine.class.getSimpleName();

    private static MyApi myApiService = null;

    public TestAppEngine() {

        /*
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
                }); */
        // end options for devappserver

        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                .setRootUrl("https://disco-task-719.appspot.com/_ah/api/");

        myApiService = builder.build();
    }

    public void testInitialization() throws Throwable {
        initializeEvents();
        initializeGroups();
        initializeAbouts();
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


            event = new Event();
            date = format.parse("2015-09-10");
            event.setDate(new DateTime(date));
            event.setSpeaker("Patricia Bottero St-Jean");
            event.setTopic("The Virtues of Semi-Absentee Business Ownership");
            event.setDesc("Explore the business models of semi-absentee ownership. Whether you are currently employed or are looking for new opportunities, semi-absentee business ownership is a smart way to mitigate the risks of job insecurity. Celebrate unemployment and use this time as an opportunity to get a semi- absentee business off the ground. Choose one that will continue to grow, and produce income, when you return to corporate work, and if you choose to return to work.\n" +
                    "\n" +
                    " About Patricia Bottero St-Jean\n" +
                    "\n" +
                    "Patricia Bottero St-Jean is a Business Career Coach with over twenty years of experience in business and entrepreneurship. Patricia holds a BA in Political Economy from UC Berkeley and is MBA candidate in Global Innovation at CSU East Bay.\n" +
                    "\n" +
                    "In her early twenties Patricia sought the American Dream in which freedom includes the opportunity for prosperity, and an upward social mobility. Thus she first experienced self-employment as an immigrant to the United States from Europe. For the next twenty+ years she grew a business to a thriving enterprise that employed up to 50.  Business ownership gave her everything she dreamed of: citizenship to a country she loves, personal growth, education, life-long relationships, the flexibility to be a fully available mom, a lifestyle of her own design, and the income to support it all. Yes, she worked hard too, but everyday Patricia had the rare privilege of working on her business, her dream. In the process, she grew a dynamic enterprise and gained tremendous knowledge as an entrepreneur.\n" +
                    "\n" +
                    "Today she continues to live her vision of an empowered life by coaching people explore the less understood but more rewarding, and worth working for, path of achieving income, lifestyle, wealth and equity: entrepreneurship!");
            event.setImage("http://csix.org/wp-content/uploads/2015/08/PatriciaBotteroStJean-e1440204266895.jpg");
            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-10-01");
            event.setDate(new DateTime(date));
            event.setSpeaker("Debbie Melnikoff");
            event.setTopic("Writing Powerful LinkedIn Profiles");
            event.setDesc("\n" +
                    "The days of copying and pasting your resume into LinkedIn are gone. Today you need much more to stand out among the 364+ million LinkedIn users. Your profile must be optimized to land at the top of recruiter search results.\n" +
                    "\n" +
                    "During this presentation you will learn new writing strategies to blend personality and SEO into your LinkedIn profile; make your profile visually appealing by using symbols and media; and tips for writing great content to make your profile stand out and much more!\n" +
                    "\n" +
                    "Debbie is a Certified Career Coach (CCC) and founder of Third Wave Career Coaching, located in Santa Cruz, CA. She provides career transition coaching to both private and corporate clients spanning a wide variety of industries including technology, food and beverage, agriculture and non-profit.\n" +
                    "\n" +
                    "Debbie is uniquely qualified to assist clients in transition with over 20 years experience in corporate HR where she managed Talent Acquisition and Learning and Development functions as well as serving as an executive coach. Prior to opening her practice in 2009, she worked for Texas Instruments, Creative Labs, Sun Microsystems and Borland International.\n" +
                    "\n" +
                    "Debbie’s credentials include:  Masters in Career Counseling; Certified Career Coach; Certified Job Search Strategist; Certified Social Media Career Strategist; Certified Myers – Briggs Type Indicator® Practitioner.");
            event.setImage("http://csix.org/wp-content/uploads/2015/08/Debbie-Melnikoff-e1440603883556.jpg");
            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-11-05");
            event.setDate(new DateTime(date));
            event.setSpeaker("Sandra Clark");
            event.setTopic("Linking Into Career Success");
            event.setDesc("Learn advanced tips and techniques for using LinkedIn for your job search.\n " +
                    "\n" +
                    "What makes a powerful profile that will attract and hold the attention of employers\n" +
                    "How to play to your strengths on LinkedIn when you’re a mature professional\n" +
                    "How to use LI notes and tags for your job search\n" +
                    "How to use LinkedIn’s Groups and job search functions, including the advanced search\n" +
                    "Ninja job search tricks and tips\n" +
                    "Learn how to find people within your targeted companies and how to approach them, how to use LinkedIn groups to expand your professional network and how to get insider information on prospective job openings.\n" +
                    "\n" +
                    "* This presentation is best suited for people who have a solid understanding of LinkedIn, have already created a strong profile on LinkedIn and are ready to get the most out of the tool.\n" +
                    "\n" +
                    "Sandra Clark is a trainer and coach specializing in providing LinkedIn education for individuals to create and manage a LinkedIn presence that supports their professional goals or for businesses to maximize their visibility with the collaboration of their employees. Sandra started her career as a teacher and theatre director before moving into educational administration, working for the University of California system (bothUC Berkeley Extension and UCSC Extension) providing corporate training to Silicon Valley for 25 years before starting her own company, LinkedIn Mentoring.\n" +
                    "\n" +
                    "www.LinkedInMentoring.com");
            event.setImage("http://csix.org/wp-content/uploads/2015/05/SandraClark.jpg");
            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-10-22");
            event.setDate(new DateTime(date));
            event.setSpeaker("Clara Chorley");
            event.setTopic("3 Big Mistakes That Cause Job Seekers to Lose Motivation, Focus and Not Find The Job They Want");
            event.setImage("http://csix.org/wp-content/uploads/2015/08/ClaraChorley.jpg");
            event.setDesc("The longer people find themselves out of work, the harder it can get to maintain focus, energy and motivation. Over time, taking steps results in little or no progress, obstacles appear that seem impossible to get around, and a sense of stuckness begins to settle in.\n" +
                    "\n" +
                    "Job success is largely determined by three things:\n" +
                    "\n" +
                    "– Making sure what we’re doing aligns with our personal values\n" +
                    "\n" +
                    "– Knowing how to strategically access and utilize resources and connections\n" +
                    "\n" +
                    "– Being willing to grow, change and adapt – as our work situation has\n" +
                    "\n" +
                    "Signs that indicate overwork and overwhelm is here or on the way:\n" +
                    "\n" +
                    "– Stress and anxiety are daily companions\n" +
                    "\n" +
                    "– Doing ‘more’ doesn’t generate the desired results\n" +
                    "\n" +
                    "– It’s really difficult to see the long term vision\n" +
                    "\n" +
                    "– Buying that plane ticket to Hawaii is getting to be a very real option – if only it was affordable!\n" +
                    "\n" +
                    "What you’ll take away:\n" +
                    "\n" +
                    "– Tools to help you avoid the 3 Massive Mistakes\n" +
                    "\n" +
                    "– Clarity about your next steps\n" +
                    "\n" +
                    "– Insight into how you are getting in your own way, and practical steps to get out of the way\n" +
                    "\n" +
                    "– Energy and inspiration to get you moving again\n" +
                    "\n" +
                    "Clara Chorley helps professional women and men around the world gain clarity about what’s next in their careers, and then integrate those changes into the rest of their lives.\n" +
                    "\n" +
                    "She is the CEO and founder of Clarity Unlimited; and has an extensive and unique international background as a career transition coach, professional speaker, humanitarian, and insatiable explorer.\n" +
                    "\n" +
                    "Clara has worked and travelled through 40 countries and 5 continents. She is an international speaker, TEDx presenter, and author of the best-seller T.U.R.N.: 4 Steps to Clarity in your Life and Career. Clara currently lives in San Francisco, California.");

            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-09-17");
            event.setDate(new DateTime(date));
            event.setSpeaker("Dan Rink");
            event.setTopic("Beyond Job Search Documents – Way Beyond!");
            event.setImage("http://csix.org/wp-content/uploads/2015/09/DanRink.png");
            event.setDesc("Many managers and professionals get stalled in their job search because of so much conflicting advice about job search documents and about all of the other elements of a successful job search. The key is just to get the documents out of the way – keeping them simple and focused. Then address the other issues that keep people stuck. We’ll provide a brief overview of the basic documents and provide free PDF tutorials following the talk. Most of this talk will then address broader issues like focus, motivation, choices, networking, success teams, and relationships.\n" +
                    "\n" +
                    "Speaker’s Bio:\n" +
                    "\n" +
                    " \n" +
                    "\n" +
                    "Dan has facilitated hundreds of workshops on job search, career management, knowledge strategies, and communication skills. He has also coached a wide range of managers andprofessionals through career transitions and entrepreneurial issues. Throughout his career, Dan has taught managers and professionals how to enhance their productivity and retain their flexibility in a rapidly changing world.");

            myApiService.insertEvent(event).execute();

            event = new Event();
            date = format.parse("2015-10-08");
            event.setDate(new DateTime(date));
            event.setSpeaker("Julie Anderson");
            event.setTopic("Want Success? Use Your Brain");
            event.setImage("http://csix.org/wp-content/uploads/2015/09/JulieAnderson.jpg");
            event.setDesc("All of us desire to achieve greater success and happiness in business and in life. What career or employment we seek will definitely impact just how much success and how happy we are. What field do we chose? Do we aspire to leadership or just want the 9-5? How do I make the choice that is the BEST for me? In order to make the right decisions and find the ideal career for you, you must first “know thyself”. That is know your brain. Our brains are an amazing and wonderful part of who we are. They are our most valuable asset. Each individual has a set of innate gifts that are unique to them. When we learn to maximize our natural gifts, remove the limiting beliefs and negative self-talk, we can truly begin to achieve things we never thought possible. Next, when you educate ourselves on how people think and process information this leads to understanding why they do things the way they do. In other words understanding their Brain Personality Connection. Then guess what? Success happens!\n" +
                    "In this presentation you will learn how to:\n" +
                    "\n" +
                    "*Identify our natural brain gifts and how they affect our connection to other people.\n" +
                    "*Decipher what the best career choices are for you and what positions to apply for\n" +
                    "*Discover how to best present your strengths to a prospective employer.\n" +
                    "*Maximize your effectiveness in relationships for greater happiness and success.\n" +
                    "\n" +
                    "Julie Anderson of YourBestMindOnline.com is a dynamic and engaging international public speaker. Her fun and interactive presentations make the technical science behind psychology interesting and understandable. When Julie speaks she captivates listeners with her natural, down-to-earth, and always energetic style. For more than 15 years she has been igniting her audiences to fire up their brains. Her keynotes and workshops inspire positive changes in the relationships of all in attendance. The information she shares will help those who hear to accelerate their success in life and business through discovery of their natural gifts.");

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
            group.setTime("First Tuesdays every month,\n6:00 pm - 8:30 pm");
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
            group.setLocation("Community Room – 2nd Floor,\nBascom Library & Community Center");
            group.setTime("Every 1st and 3rd Wednesday of each month,\n9:00 am – 11:00 am");
            group.setDesc("Networking, job leads, and information focused on the corporate functions of manufacturing and more general operations including Facilities, Logistics, Supply Chain, Procurement, Materials, Manufacturing & Test Engineering and Quality Engineering.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Finance SIG");
            group.setAddress("118 East El Camino Real, Sunnyvale, CA 94087");
            group.setLocation("Panera Bread, Sunnyvale");
            group.setTime("Every 2nd and 4th Wednesday of each month,\n7:30 am – 9:00 pm");
            group.setDesc("Our mission is to bring together finance professionals in the South Bay Area so that we may explore the uniqueness of working in this field and how that pertains to job searching, professional development, and being successful on the job.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Job Search Empowerment on the Peninsula (JSEP)");
            group.setAddress("399 Marine Parkway, Redwood City, CA 94065");
            group.setLocation("Meeting Room #B, Redwood Shores Library");
                    group.setTime("Monday (Except holidays),\n5:30 pm – 6:30 pm");
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
            group.setLocation("Community Room – 2nd Floor,\nBascom Library & Community Center");
            group.setTime("Every 1st and 3rd Wednesday of each month,\n9:00 am – 11:00 am");
            group.setDesc("SemiSIG is a Special Interest Group for professionals in the semiconductor industry, and in the related industries of flat panel display, magnetic, optical and thin film coating. Our network was formed to improve our members’ knowledge of these industries and to share career change opportunities.");
            myApiService.insertGroup(group).execute();

            group = new Group();
            group.setName("Morning Wildcat Loop Trail Hike");
            group.setAddress("22500 Cristo Rey Drive, Los Altos, CA");
            group.setLocation("Rancho San Antonio");
            group.setTime("Every Tuesday,\n6:30 am - 8:00 am");
            group.setDesc("A fun group of people who meet every Tuesday morning at 6:30 a.m. at Rancho San Antonio in Cupertino (off Hwy 280 & 85) for an early morning networking hike. It’s a terrific way to keep fit and meet interesting people at the same time. This is a particularly good group for people who may be in career transition mode because of the group’s philosophy of mutual support, the power of networking and a group hike! We are finished by 8:00 a.m. so you can get on with your day and feel fantastic because you have had a great workout. We hope you can join us soon!");
            myApiService.insertGroup(group).execute();
        }
    }

    public void initializeAbouts() throws Throwable {
        if (myApiService != null) {
            myApiService.removeAllAbouts().execute();

            About about = new About();
            about.setTitle("About");
            about.setDesc("CSIX CONNECT helps individuals in career transition to significantly improve their job search success through education, in-person networking and mutual support.   In today’s job market, more than 80 per cent of jobs obtained result from successful networking. CSIX CONNECT provides the means to tap into and leverage the power of a network that is already more than 7000 members strong.");
            myApiService.insertAbout(about).execute();

            about = new About();
            about.setTitle("Connect");
            about.setDesc("CSIX is built on the belief that networking is most effective when done face-to-face. Therefore, to join CSIX you must attend a meeting and sign up in person. Membership is free. CSIX membership provides access to a vast array of job listings and job search resources via other members, the CSIX CONNECT Yahoo! group.");
            myApiService.insertAbout(about).execute();
        }
    }
}
