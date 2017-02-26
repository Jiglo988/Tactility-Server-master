package org.hyperion.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: Wasay
 * Date: 12/10/14
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public final class ArrayUtils {


    public static <T> boolean contains(T needle, T... array) {
        return contains(Predicate.isEqual(needle), array);
    }

    public static <T> boolean contains(Predicate<? super T> predicate, T... array) {
        return Stream.of(array).filter(Objects::nonNull).anyMatch(predicate);
    }

    public static int[] fromInteger(final Integer[] integers) {
        final int[] values = new int[integers.length];
        for(int index = 0; index < values.length; index++)
            values[index] = integers[index];
        return values;
    }

    public static int[] fromList(final List<Integer> list) {
        return fromInteger(list.toArray(new Integer[list.size()]));
    }

    public static boolean contains(final int i, final int... ints) {
        for(final int integer : ints)
            if(i == integer)
                return true;
        return false;
    }

}
