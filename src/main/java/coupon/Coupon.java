package coupon;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

import static coupon.CouponStatus.ISSUED;

public final class Coupon {
    private final UUID id;
    private final UUID memberId;
    private final CouponType type;
    private final Instant issuedAt;
    private final Instant validUntil;
    private final CouponStatus status;
    private final String code;
    private final String description;
    private final Instant usedAt;

    private Coupon(
            UUID id,
            UUID memberId,
            CouponType type,
            Instant issuedAt,
            Instant validUntil,
            CouponStatus status,
            String code,
            String description,
            Instant usedAt
    ) {
        this.id = id;
        this.memberId = memberId;
        this.type = type;
        this.issuedAt = issuedAt;
        this.validUntil = validUntil;
        this.status = status;
        this.code = code;
        this.description = description;
        this.usedAt = usedAt;
    }

    public static Coupon of(final UUID memberId, final CouponType type, final String code, final String description) {
        final var issuedAt = Instant.now();
        return new Coupon(UUID.randomUUID(), memberId, type, issuedAt, issuedAt.plus(7L, ChronoUnit.DAYS), ISSUED, code, description, null);
    }

    public UUID id() {
        return id;
    }

    public UUID memberId() {
        return memberId;
    }

    public CouponType type() {
        return type;
    }

    public Instant issuedAt() {
        return issuedAt;
    }

    public Instant validUntil() {
        return validUntil;
    }

    public CouponStatus status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public Instant usedAt() {
        return usedAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Coupon) obj;
        return Objects.equals(this.id, that.id) &&
                this.memberId == that.memberId &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.issuedAt, that.issuedAt) &&
                Objects.equals(this.validUntil, that.validUntil) &&
                Objects.equals(this.status, that.status) &&
                Objects.equals(this.code, that.code) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.usedAt, that.usedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, memberId, type, issuedAt, validUntil, status, code, description, usedAt);
    }

    @Override
    public String toString() {
        return "Coupon[" +
                "id=" + id + ", " +
                "memberId=" + memberId + ", " +
                "type=" + type + ", " +
                "issuedAt=" + issuedAt + ", " +
                "validUntil=" + validUntil + ", " +
                "status=" + status + ", " +
                "code=" + code + ", " +
                "description=" + description + ", " +
                "usedAt=" + usedAt + ']';
    }

}

