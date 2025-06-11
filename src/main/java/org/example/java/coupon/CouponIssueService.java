package org.example.java.coupon;

import org.example.java.member.Member;

import java.util.List;
import java.util.stream.Collectors;

public class CouponIssueService {

    private final CouponStore couponStore;

    public CouponIssueService(final CouponStore couponStore) {
        this.couponStore = couponStore;
    }

    public List<Coupon> getCoupons(final Member member) {
        final var issuedCouponTypeSet = couponStore.findIssuedCouponsByMemberId(member.id()).stream()
                .map(Coupon::type)
                .collect(Collectors.toSet());

        final var remainingCouponCountMap = couponStore.remainingCountMap();

        final var list = getByMemberGrade(member).stream()
                .filter(coupon -> !issuedCouponTypeSet.contains(coupon.type()))
                .filter(coupon -> remainingCouponCountMap.get(coupon.type()) > 0).toList();

        return couponStore.issue(list);
    }

    private static List<Coupon> getByMemberGrade(final Member member) {
        return switch (member.grade()) {
            case BRONZE -> List.of();
            case SILVER -> List.of(
                    Coupon.of(member.id(), CouponType.FREE_DELIVERY, "DDDD", "무료배송")
            );
            case GOLD, PLATINUM -> List.of(
                    Coupon.of(member.id(), CouponType.FREE_DELIVERY, "DDDD", "무료배송"),
                    Coupon.of(member.id(), CouponType.TEN_PERCENT, "DDDD", "10% 할인")
            );
        };
    }


}
