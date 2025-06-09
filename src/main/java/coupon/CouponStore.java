package coupon;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CouponStore {

    List<Coupon> findIssuedCouponsByMemberId(UUID memberId);

    Map<CouponType, Integer> remainingCountMap();

    List<Coupon> issue(List<Coupon> coupons);
}
