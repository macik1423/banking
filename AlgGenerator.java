package banking;

import java.util.Arrays;

public class AlgGenerator {
    public AlgGenerator() {
    }

    long generateCheckSumDigit(String number) {
        int[] numbers = new int[number.length()];

        //first step
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                numbers[i] = digit * 2;
            } else {
                numbers[i] = digit;
            }
        }

        //second step
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 9) {
                numbers[i] -= 9;
            }
        }

        //third step
        long sum = Arrays.stream(numbers).asLongStream().reduce((x, y) -> x + y).getAsLong();

        long checkSumDigit = 0;
        if (sum % 10 != 0) {
            checkSumDigit = 10 - sum % 10;
        }
        return checkSumDigit;
    }

    public boolean checkLuhn(String cardNo)
    {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}