import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.random.RandomGenerator;

public class RandomTestUtils {

    private static final RandomGenerator random = RandomGenerator.getDefault();

    public static String randomAlphaString() {
        return randomStringFromCharset(random.nextInt(1), "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    }

    public static String randomNumericString() {
        return randomStringFromCharset(random.nextInt(), "0123456789");
    }

    public static double randomDouble() {
        return random.nextDouble();
    }

    public static long randomLong() {
        return random.nextLong();
    }

    public static LocalDateTime randomLocalDateTime() {
        // 기준 시간: 현재
        LocalDateTime now = LocalDateTime.now();

        // 총 범위: 과거 10년 ~ 미래 10년 (일 단위)
        long daysRange = 10 * 365L * 2; // 7300일
        long randomDays = random.nextLong(-10 * 365L, 10 * 365L + 1);

        // 시간, 분, 초 단위도 랜덤
        int randomHour = random.nextInt(24);
        int randomMinute = random.nextInt(60);
        int randomSecond = random.nextInt(60);

        return now.plusDays(randomDays)
                .withHour(randomHour)
                .withMinute(randomMinute)
                .withSecond(randomSecond)
                .truncatedTo(ChronoUnit.SECONDS);
    }

    private static String randomStringFromCharset(int length, String charset) {
        StringBuilder sb = new StringBuilder(length);
        int bound = charset.length();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(bound);
            sb.append(charset.charAt(index));
        }
        return sb.toString();
    }

}
