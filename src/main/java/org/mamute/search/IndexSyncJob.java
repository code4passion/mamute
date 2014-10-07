package org.mamute.search;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.VRaptorInitialized;

import org.hibernate.Session;
import org.mamute.brutauth.auth.rules.ModeratorOnlyRule;
import org.mamute.dao.InvisibleForUsersRule;
import org.mamute.dao.QuestionDAO;
import org.mamute.model.LoggedUser;
import org.mamute.model.Question;
import org.mamute.model.User;
import org.mamute.providers.SessionFactoryCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import java.util.List;

import static org.mamute.model.SanitizedText.fromTrustedText;

@Controller @ApplicationScoped
@CustomBrutauthRules(ModeratorOnlyRule.class)
public class IndexSyncJob {
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexSyncJob.class);

	private SessionFactoryCreator factory;
	private QuestionIndex index;
	private Environment environment;

	@Deprecated
	protected IndexSyncJob() {
	}

	@Inject
	public IndexSyncJob(SessionFactoryCreator factory, QuestionIndex index, Environment environment) {
		this.factory = factory;
		this.index = index;
		this.environment = environment;
	}

	@Post("/akwiqeojovndfasf0asf0s9ad8fas9d")
	public void execute() {
		Session session = factory.getInstance().openSession();
		try {
			QuestionDAO questions = new QuestionDAO(session, generateUser());

			long pages = questions.numberOfPages();
			long total = 0;
			LOGGER.info("Syncing questions!");
			for (int i = 0; i < pages; i++) {
				List<Question> q = questions.allVisible(i);
				index.indexQuestionBatch(q);
				total += q.size();
			}
			LOGGER.info("Synced " + total + " questions");
		} finally {
			session.close();
		}
	}

	@Post("/akwiqeojovndfasf0asf0s9ad8fas9d12io3nwo120")
	public void onStartup(@Observes VRaptorInitialized init) {
		if (environment.supports("solr.syncOnStartup") && environment.supports("feature.solr")) {
			execute();
		}
	}

	private InvisibleForUsersRule generateUser() {
		User user = new User(fromTrustedText("System"), "system");
		LoggedUser loggedUser = new LoggedUser(user, null);
		return new InvisibleForUsersRule(loggedUser);
	}
}