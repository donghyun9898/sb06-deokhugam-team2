package com.codeit.sb06deokhugamteam2.dashboard.fixture;

import com.codeit.sb06deokhugamteam2.book.entity.Book;
import com.codeit.sb06deokhugamteam2.common.enums.PeriodType;
import com.codeit.sb06deokhugamteam2.common.enums.RankingType;
import com.codeit.sb06deokhugamteam2.dashboard.entity.Dashboard;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLike;
import com.codeit.sb06deokhugamteam2.like.adapter.out.entity.ReviewLikeId;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.ReviewStat;
import com.codeit.sb06deokhugamteam2.user.entity.User;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

public class DashboardFixture {

    public static Dashboard createDashboard(int count, UUID reviewId) {

        Dashboard dashboard = Dashboard.builder()
                .rank((long) count)
                .score((double) count)
                .entityId(reviewId)
                .periodType(PeriodType.DAILY)
                .rankingType(RankingType.REVIEW)
                .build();
        setDashboardCreatedAt(dashboard, Instant.now().minus(1, ChronoUnit.DAYS));
        return dashboard;
    }

    public static User createUser(int count, List<Review> reviews) {
        User user = User.builder()
                .email("test" + count + "@naver.com")
                .comments(null)
                .reviews(reviews)
                .nickname("nickname" + count)
                .password("password" + count)
                .build();

        for (Review review : reviews) {
            review.user(user);
        }

        return user;
    }

    public static Review createReview(int count, Book book) {
        Review review = new Review();
        review.deleted(false);
        review.rating(count % 5);
        review.reviewStat(createReviewStat(review));
        review.content("test review" + count);
        review.book(book);
        review.id(UUID.randomUUID());
        review.createdAt(Instant.now().minus(1, ChronoUnit.DAYS));
        review.updatedAt(Instant.now().minus(1, ChronoUnit.DAYS));
        return review;
    }

    public static ReviewLike createReviewLike(Review review, User user) {
        ReviewLikeId id = new ReviewLikeId();
        id.reviewId(review.id());
        id.userId(user.getId());
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.user(user);
        reviewLike.review(review);
        reviewLike.likedAt(Instant.now().minus(1, ChronoUnit.DAYS));
        reviewLike.id(id);
        return reviewLike;
    }

    private static ReviewStat createReviewStat(Review review) {
        ReviewStat reviewStat = new ReviewStat();
        reviewStat.review(review);
        reviewStat.commentCount(0);
        reviewStat.likeCount(0);
        return reviewStat;
    }

    private static void setDashboardCreatedAt(Dashboard dashboard, Instant createdAt) {
        try {
            Field field = Dashboard.class.getDeclaredField("createdAt");
            field.setAccessible(true);
            field.set(dashboard, createdAt);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
