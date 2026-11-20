package com.oziel.os.data;
import com.oziel.os.entity.*;
import com.oziel.os.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.*;
import java.util.List;

@Component @RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final ProjectRepository projects;
    private final BuildLogRepository logs;
    private final SkillRepository skills;
    private final OpportunityRepository opportunities;
    private final ImpactStatRepository impactStats;

    @Override public void run(String... args) {
        if (projects.count()==0) {
            projects.saveAll(List.of(
            Project.builder().title("Oziel OS").slug("oziel-os").problem("Varsity talent is too often presented as a CV instead of a measurable body of work.")
                .solution("A live Java platform that turns projects, learning, skills and career activity into verifiable proof.")
                .techStack(List.of("Java 21","Spring Boot","React","PostgreSQL")).status(ProjectStatus.LIVE).githubUrl("https://github.com")
                .demoUrl("/").impact("One production-grade career system").featured(true).build(),
            Project.builder().title("Township Learn").slug("township-learn").problem("Learners need accessible study tools that work in low-connectivity environments.")
                .solution("A lightweight learning workflow for notes, quizzes and progress feedback.")
                .techStack(List.of("Java","Spring","React","PostgreSQL")).status(ProjectStatus.IN_PROGRESS).impact("Built for 50+ learner sessions").featured(true).build(),
            Project.builder().title("SA Career Radar").slug("sa-career-radar").problem("Students miss high-value opportunities because applications are scattered.")
                .solution("A single opportunity pipeline for bursaries, internships and graduate programmes.")
                .techStack(List.of("Java","JPA","TanStack Query")).status(ProjectStatus.CONCEPT).impact("Centralises the application journey").featured(true).build()
            ));
            var now=Instant.now();
            if (logs.count()==0) logs.saveAll(List.of(
            BuildLog.builder().title("Spring Boot architecture in place").content("Established a clean controller-service-repository structure, validated entities and prepared the API contract for OZIEL OS.")
                .category(LogCategory.CODE).tags("Java,Spring Boot,Docker").createdAt(now.minus(Duration.ofDays(1))).build(),
            BuildLog.builder().title("Designed for an SA hiring manager").content("Focused the system on measurable proof: live projects, consistent learning and a visible application pipeline.")
                .category(LogCategory.CAREER).tags("Portfolio,Strategy").createdAt(now.minus(Duration.ofDays(4))).build(),
            BuildLog.builder().title("PostgreSQL deployment model").content("Documented local and hosted database configuration so the same JPA entities work in development and production.")
                .category(LogCategory.LEARNING).tags("PostgreSQL,JPA").createdAt(now.minus(Duration.ofDays(8))).build()
            ));
            if (skills.count()==0) skills.saveAll(List.of(
            Skill.builder().name("Java 21").category(SkillCategory.BACKEND).level(SkillLevel.ADVANCED).confidence(88).proofUrl("/proof").build(),
            Skill.builder().name("Spring Boot").category(SkillCategory.BACKEND).level(SkillLevel.ADVANCED).confidence(86).proofUrl("/proof/oziel-os").build(),
            Skill.builder().name("React + TypeScript").category(SkillCategory.FRONTEND).level(SkillLevel.INTERMEDIATE).confidence(78).proofUrl("/proof").build(),
            Skill.builder().name("PostgreSQL").category(SkillCategory.DATA).level(SkillLevel.INTERMEDIATE).confidence(72).proofUrl("/proof").build(),
            Skill.builder().name("Docker").category(SkillCategory.CLOUD_TOOLS).level(SkillLevel.INTERMEDIATE).confidence(70).proofUrl("/proof").build()
            ));
        }
        seedAdditionalProjects();
        seedAdditionalCodeLogs();
        if (opportunities.count()==0) opportunities.saveAll(List.of(
            Opportunity.builder().name("FNB App Academy").company("FNB").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(2)).status(OpportunityStatus.WISHLIST).link("https://www.fnb.co.za/careers").build(),
            Opportunity.builder().name("Technology Graduate Programme").company("Standard Bank").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(3)).status(OpportunityStatus.WISHLIST).link("https://www.standardbank.com/southafrica/careers").build(),
            Opportunity.builder().name("Technology Internship").company("Capitec").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(1)).status(OpportunityStatus.APPLIED).link("https://www.capitecbank.co.za/careers").build(),
            Opportunity.builder().name("Investec Technology Bursary").company("Investec").type(OpportunityType.BURSARY).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.investec.com/en_ZA/careers/bursaries.html").build(),
            Opportunity.builder().name("NSFAS Bursary").company("National Student Financial Aid Scheme").type(OpportunityType.BURSARY).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.nsfas.org.za").build(),
            Opportunity.builder().name("Nedbank Quants Graduate Programme").company("Nedbank").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(3)).status(OpportunityStatus.WISHLIST).link("https://www.nedbank.co.za/content/nedbank/desktop/gt/en/careers.html").build(),
            Opportunity.builder().name("Absa Graduate Development Programme").company("Absa").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.absa.africa/careers").build(),
            Opportunity.builder().name("Discovery IT Graduate Programme").company("Discovery").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(2)).status(OpportunityStatus.WISHLIST).link("https://www.discovery.co.za/corporate/careers").build(),
            Opportunity.builder().name("Vodacom Discover Graduate Programme").company("Vodacom").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://careers.vodafone.com/vodacom-south-africa").build(),
            Opportunity.builder().name("MTN Graduate Development Programme").company("MTN").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(3)).status(OpportunityStatus.WISHLIST).link("https://www.mtn.com/careers").build(),
            Opportunity.builder().name("Sasol Bursary Programme").company("Sasol").type(OpportunityType.BURSARY).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://www.sasol.com/careers").build(),
            Opportunity.builder().name("Eskom Technology Bursary").company("Eskom").type(OpportunityType.BURSARY).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.eskom.co.za/careers").build(),
            Opportunity.builder().name("SITA Internship Programme").company("SITA").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(2)).status(OpportunityStatus.WISHLIST).link("https://www.sita.co.za/careers").build(),
            Opportunity.builder().name("EEP Learnership").company("EEP / ICT SETA").type(OpportunityType.LEARNERSHIP).closingDate(LocalDate.now().plusMonths(2)).status(OpportunityStatus.WISHLIST).link("https://www.ictsector.co.za").build(),
            Opportunity.builder().name("ICT Learnership Programme").company("MICT SETA").type(OpportunityType.LEARNERSHIP).closingDate(LocalDate.now().plusMonths(3)).status(OpportunityStatus.WISHLIST).link("https://www.mict.org.za").build(),
            Opportunity.builder().name("Microsoft LEAP Apprenticeship").company("Microsoft").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://www.microsoft.com/leap").build(),
            Opportunity.builder().name("Google STEP Internship").company("Google").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(6)).status(OpportunityStatus.WISHLIST).link("https://buildyourfuture.withgoogle.com/programs/step").build(),
            Opportunity.builder().name("Google Summer of Code").company("Google Open Source").type(OpportunityType.HACKATHON).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://summerofcode.withgoogle.com").build(),
            Opportunity.builder().name("Amazon Future Engineer").company("Amazon").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://www.amazonfutureengineer.com").build(),
            Opportunity.builder().name("Amazon Student Internship").company("Amazon").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(6)).status(OpportunityStatus.WISHLIST).link("https://www.amazon.jobs/students").build(),
            Opportunity.builder().name("Microsoft Learn Student Ambassador").company("Microsoft").type(OpportunityType.HACKATHON).closingDate(LocalDate.now().plusMonths(12)).status(OpportunityStatus.WISHLIST).link("https://studentambassadors.microsoft.com").build(),
            Opportunity.builder().name("AWS Educate").company("Amazon Web Services").type(OpportunityType.LEARNERSHIP).closingDate(LocalDate.now().plusMonths(12)).status(OpportunityStatus.WISHLIST).link("https://aws.amazon.com/education/awseducate").build(),
            Opportunity.builder().name("Rhodes University Mandela Rhodes Scholarship").company("Mandela Rhodes Foundation").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://www.mandelarhodes.org").build(),
            Opportunity.builder().name("Mastercard Foundation Scholars Program").company("Mastercard Foundation").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(7)).status(OpportunityStatus.WISHLIST).link("https://mastercardfdn.org").build(),
            Opportunity.builder().name("DAAD Development Scholarships").company("DAAD / German Academic Exchange").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(8)).status(OpportunityStatus.WISHLIST).link("https://www.daad.de/en").build(),
            Opportunity.builder().name("Erasmus Mundus Joint Masters").company("European Commission").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://erasmus-plus.ec.europa.eu").build(),
            Opportunity.builder().name("Chevening Scholarships").company("UK Foreign, Commonwealth & Development Office").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.chevening.org").build(),
            Opportunity.builder().name("Commonwealth Master's Scholarship").company("Commonwealth Scholarship Commission").type(OpportunityType.SCHOLARSHIP).closingDate(LocalDate.now().plusMonths(6)).status(OpportunityStatus.WISHLIST).link("https://cscuk.fcdo.gov.uk").build(),
            Opportunity.builder().name("Accenture Technology Internship").company("Accenture").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(3)).status(OpportunityStatus.WISHLIST).link("https://www.accenture.com/za-en/careers").build(),
            Opportunity.builder().name("PwC Technology Graduate Programme").company("PwC South Africa").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.pwc.co.za/en/careers.html").build(),
            Opportunity.builder().name("Deloitte Technology Graduate Programme").company("Deloitte").type(OpportunityType.JOB).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://www.deloitte.com/za/en/careers").build(),
            Opportunity.builder().name("NVIDIA Graduate and Internship Roles").company("NVIDIA").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(6)).status(OpportunityStatus.WISHLIST).link("https://nvidia.wd5.myworkdayjobs.com/NVIDIAExternalCareerSite").build(),
            Opportunity.builder().name("Meta University Internship").company("Meta").type(OpportunityType.INTERNSHIP).closingDate(LocalDate.now().plusMonths(7)).status(OpportunityStatus.WISHLIST).link("https://www.metacareers.com").build(),
            Opportunity.builder().name("SAP Young Professionals Programme").company("SAP").type(OpportunityType.LEARNERSHIP).closingDate(LocalDate.now().plusMonths(4)).status(OpportunityStatus.WISHLIST).link("https://jobs.sap.com").build(),
            Opportunity.builder().name("UCT, Wits and UP Technology Bursaries").company("South African Universities").type(OpportunityType.BURSARY).closingDate(LocalDate.now().plusMonths(6)).status(OpportunityStatus.WISHLIST).link("https://www.nsfas.org.za").build(),
            Opportunity.builder().name("Girls Who Code Summer Immersion").company("Girls Who Code").type(OpportunityType.HACKATHON).closingDate(LocalDate.now().plusMonths(5)).status(OpportunityStatus.WISHLIST).link("https://girlswhocode.com/programs").build()
        ));
        if (impactStats.count()==0) impactStats.saveAll(List.of(
            ImpactStat.builder().label("Students Mentored").value(12).description("Peers supported with Java, Git and interview preparation").build(),
            ImpactStat.builder().label("Notes Shared").value(28).description("Practical engineering notes shared with the community").build(),
            ImpactStat.builder().label("People Helped").value(36).description("Direct and peer-to-peer support delivered").build(),
            ImpactStat.builder().label("Events Attended").value(8).description("Technology, career and community events").build()
        ));
    }

    private void seedAdditionalProjects() {
        var briefs=List.of(
            Project.builder().title("Community Skills Support Portal").slug("community-skills-support")
                .problem("Students and volunteer mentors often coordinate practical tech support across scattered messages, making follow-ups difficult to track.")
                .solution("A mobile-first portal concept for submitting support requests, matching mentors and tracking progress with clear privacy boundaries.")
                .techStack(List.of("Java 21","Spring Boot","React","TypeScript","PostgreSQL"))
                .status(ProjectStatus.CONCEPT).impact("Concept brief - implementation not started").build(),
            Project.builder().title("SA Tech Opportunity Insights").slug("sa-tech-opportunity-insights")
                .problem("Opportunity listings are fragmented, and students have little visibility into recurring role types, requested skills and application timelines.")
                .solution("A data product concept that normalises source-linked public listings and presents transparent filters and aggregate trends without collecting applicant personal data.")
                .techStack(List.of("Java 21","Spring Boot","PostgreSQL","SQL","Data Analytics","Recharts"))
                .status(ProjectStatus.CONCEPT).impact("Concept brief - implementation not started").build(),
            Project.builder().title("Java Cloud Deployment Lab").slug("java-cloud-deployment-lab")
                .problem("A service that runs locally still needs repeatable packaging, safe configuration and observable deployment checks.")
                .solution("A cloud engineering concept for containerising a Java API, running automated checks and deploying with environment-based configuration and health monitoring.")
                .techStack(List.of("Java 21","Spring Boot","Docker","AWS","GitHub Actions"))
                .status(ProjectStatus.CONCEPT).impact("Concept brief - implementation not started").build()
        );
        briefs.forEach(project -> {
            if(projects.findBySlug(project.getSlug()).isEmpty()) projects.save(project);
        });
    }

    private void seedAdditionalCodeLogs() {
        var entries=List.of(
            BuildLog.builder().title("Queued email alerts for new opportunities")
                .content("Added an email delivery outbox for opportunities entered in the tracker, with visible delivery states and retry handling. SMS remains disabled unless explicitly configured.")
                .category(LogCategory.CODE).tags("Java,Spring Boot,Email,Notifications").build(),
            BuildLog.builder().title("Added Web, Data and Cloud proof briefs")
                .content("Expanded the Proof Wall with clearly marked project concepts for web products, opportunity analytics and Java cloud deployment. They are labelled as concepts rather than completed work.")
                .category(LogCategory.CODE).tags("React,PostgreSQL,AWS,Portfolio").build()
        );
        entries.forEach(entry -> {
            boolean exists=logs.findAll().stream().anyMatch(existing -> existing.getTitle().equals(entry.getTitle()));
            if(!exists) logs.save(entry);
        });
    }
}
