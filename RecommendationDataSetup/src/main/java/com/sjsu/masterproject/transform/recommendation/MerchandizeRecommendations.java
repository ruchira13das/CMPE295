/**
 *
 */
package com.sjsu.masterproject.transform.recommendation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sjsu.masterproject.transform.entity.Recommendations;
import com.sjsu.masterproject.transform.repository.RecommendationsRepository;

/**
 * @author sambits
 *
 */
public abstract class MerchandizeRecommendations {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	protected abstract File getInputFile();

	protected abstract String getCategory();

	public void generateRecommendations(RecommendationsRepository repository) {
		log.info("Start generating the recommendation model for: {}", getInputFile().getName());

		try {
			long startTime = System.nanoTime();
			DataModel dataModel = new FileDataModel(getInputFile());
			ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);
			ItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

			LongPrimitiveIterator users = dataModel.getUserIDs();
			log.info("User count: {}", dataModel.getNumUsers());
			int i = 0;

			List<Recommendations> recList = new ArrayList<>();

			while (users.hasNext()) {
				Long userId = users.next();
				List<String> recommendedProducts = new ArrayList<>();
				for (RecommendedItem recommendedItem : recommender.recommend(userId, 10)) {
					// log.info("Recommendation for user: {} ::: {}", userId,
					// recommendedItem.getItemID());
					recommendedProducts.add(String.valueOf(recommendedItem.getItemID()));
				}

				// Add to Mongo
				if (recommendedProducts.size() > 0) {
					i++;
					Recommendations rec = new Recommendations();
					rec.setUserId(String.valueOf(userId));
					rec.setCategory(getCategory());
					rec.setRecommendations(recommendedProducts);

					recList.add(rec);
				}

				if (recList.size() > 0 && recList.size() % 1000 == 0) {
					// FLush it out to the DB after every 1000 records
					int count = repository.insert(recList).size();
					log.info("{} records inserted...", count);
					recList = new ArrayList<>();
				}
			}

			// Flush the remaining records to DB
			if (recList.size() > 0) {
				int count = repository.insert(recList).size();
				log.info("{} records inserted...", count);
			}

			log.info("Records inserted :: " + i);

			log.info("Recommendations generated in {} ms", (System.nanoTime() - startTime) / 1000000);

		} catch (Exception e) {
			log.error("Exception while generating recommendations!", e);
		}
	}
}
