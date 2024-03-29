package org.openconfig.util;

/**
 * Simple assertion class for usage inside openconfig. Not recommended for general use.
 *
 * @author Dushyanth (Dee) Inguva
 */
public class Assert {

    /**
     * A simple assert.
     *
     * @param condition
     * @param message The message can contain placeholder values that need to be substituted
     * @param placeholderValues the placeholder values that will be substituted if the condition is false
     * @throws IllegalArgumentException if the condition is false
     */
    public static void isTrue(boolean condition, String message, Object... placeholderValues) {
        if (!condition) {
            throw new IllegalArgumentException("AssertionFailed: " + String.format(message, placeholderValues));
        }
    }

    /**
     * Ensures that the nonNullObject parameter is not null.
     *
     * @param nonNullObject the object which will be checked
     * @param message the message used to create the exception. This message can contain placeholders which should be
     * passed as <param>placeholderValues</param>
     * @param placeholderValues the place holder values which will be substituted
     * @throws IllegalArgumentException if the nonNullObject parameter is null
     */
    public static void notNull(Object nonNullObject, String message, Object... placeholderValues) {
        if (nonNullObject == null) {
            throw new IllegalArgumentException("AssertionFailed: " + String.format(message, placeholderValues));
        }
    }

    /**
     * Asserts that the passed String is not null and has non white space characters.
     *
     * @param aString the string to check
     * @param message the message used to create the exception. This message can contain placeholders which should be
     * passed as <param>placeholderValues</param>
     * @param placeholderValues the place holder values which will be substituted
     * @throws IllegalArgumentException if aString is null, empty or contains just white spaces
     */
    public static void hasLength(String aString, String message, Object... placeholderValues) {
        isTrue(aString != null, message, placeholderValues);
        isTrue(aString.trim().length() > 0, message, placeholderValues) ;
    }
}
