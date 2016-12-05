package com.sjsu.masterproject.transform.user;

import org.apache.mahout.cf.taste.impl.model.MemoryIDMigrator;

import com.google.gson.Gson;
import com.sjsu.masterproject.transform.DataTransformer;

public class UserDataTransformer extends DataTransformer {

	@Override
	protected String getTransformedLine(String inputJson, String category) throws Exception {
		MemoryIDMigrator migrator = new MemoryIDMigrator();
		Gson gson = new Gson();

		RawJsonUser rawJsonUser = gson.fromJson(inputJson, RawJsonUser.class);

		User user = new User();
		user.set_Id(rawJsonUser.getReviewerID());
		user.setIdAsLongString(String.valueOf(migrator.toLongID(rawJsonUser.getReviewerID())));
		user.setFirstName(rawJsonUser.getReviewerName());
		user.setLastName("");
		user.setPassword("sjsu@123");

		return gson.toJson(user);
	}

	@Override
	protected String getInputLoc() {
		return "data/user/raw/";
	}

	@Override
	protected String getOutputLoc() {
		return "data/user/processed/";
	}

	class RawJsonUser {
		String reviewerID;
		String reviewerName;

		public RawJsonUser() {
			super();
		}

		public RawJsonUser(String reviewerID, String reviewerName) {
			super();
			this.reviewerID = reviewerID;
			this.reviewerName = reviewerName;
		}

		@Override
		public String toString() {
			return "RawJsonProduct [reviewerID=" + reviewerID + ", reviewerName=" + reviewerName + "]";
		}

		public String getReviewerID() {
			return reviewerID;
		}

		public void setReviewerID(String reviewerID) {
			this.reviewerID = reviewerID;
		}

		public String getReviewerName() {
			return reviewerName;
		}

		public void setReviewerName(String reviewerName) {
			this.reviewerName = reviewerName;
		}

	}

	@Override
	protected String getTransformedLine(String input) throws Exception {
		// No impl required
		return null;
	}

}
