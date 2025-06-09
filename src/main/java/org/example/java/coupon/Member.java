package org.example.java.coupon;

import java.util.Objects;
import java.util.UUID;

public final class Member {
    private final UUID id;
    private final MemberGrade grade;

    private Member(
            UUID id,
            MemberGrade grade
    ) {
        this.id = id;
        this.grade = grade;
    }

    public static Member of(final MemberGrade grade) {
        return new Member(UUID.randomUUID(), grade);
    }

    public UUID id() {
        return id;
    }

    public MemberGrade grade() {
        return grade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Member) obj;
        return this.id == that.id &&
                Objects.equals(this.grade, that.grade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, grade);
    }

    @Override
    public String toString() {
        return "Member[" +
                "id=" + id + ", " +
                "grade=" + grade + ']';
    }

}
