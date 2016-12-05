package com.sjsu.masterproject.transform;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sjsu.masterproject.transform.repository.RecommendationsRepository;

@SpringBootApplication
public class RecommendationDataSetupApplication implements CommandLineRunner {

	@Autowired
	private RecommendationsRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(RecommendationDataSetupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String[] stringIds = { "A307Y3LMCVEZS1", "A3PDJG7SOPG20N", "A1VEJBOC00C24E" };
		MemoryIDMigrator migrator = new MemoryIDMigrator();

		for (String stringId : stringIds) {
			System.out.println("Raw Value: " + stringId + " :: " + stringId.hashCode());

			long longId = migrator.toLongID(stringId);
			System.out.println("Long Value: " + longId);

			String strId = migrator.toStringID(12345);
			System.out.println("Str Value: " + strId);
			System.out.println("-------------");
		}

		// DataTransformer productTransformer = new ProductDataTransformer();
		// productTransformer.transformDataFile();
		//
		// DataTransformer userTransformer = new UserDataTransformer();
		// userTransformer.transformDataFile();
		//
		// DataTransformer recTransformer = new RecommendationDataTransformer();
		// recTransformer.transformDataFile();
		//
		// MerchandizeRecommendations babyRecommender = new
		// BabyProductsRecommendations();
		// babyRecommender.generateRecommendations(repository);
		//
		// MerchandizeRecommendations beautyRecommender = new
		// BeautyProductsRecommendations();
		// beautyRecommender.generateRecommendations(repository);
		//
		// MerchandizeRecommendations booksRecommender = new
		// BooksRecommendations();
		// booksRecommender.generateRecommendations(repository);
		//
		// MerchandizeRecommendations cellPhonesRecommender = new
		// CellPhoneAccessoriesRecommendations();
		// cellPhonesRecommender.generateRecommendations(repository);
		//
		// MerchandizeRecommendations musicalRecommender = new
		// MusicalInstrumentsRecommendations();
		// musicalRecommender.generateRecommendations(repository);
		//
		// MerchandizeRecommendations toysRecommender = new
		// ToysAndGamesRecommendations();
		// toysRecommender.generateRecommendations(repository);

	}
}
