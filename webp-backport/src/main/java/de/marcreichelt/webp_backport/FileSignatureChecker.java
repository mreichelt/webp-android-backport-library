package de.marcreichelt.webp_backport;

public class FileSignatureChecker {

    /**
     * Check if a given byte array has the necessary WebP signature in the beginning of its content.
     * The signature is 'RIFFxxxxWEBP' where 'xxxx' is the file size.
     *
     * @param image the byte array to check
     * @return true if the byte array has the WebP signature else false
     */
    public static boolean checkForWebP(byte[] image) {
        return checkForRIFF(image) // starts with 'RIFF'
                && containsAt(image, new byte[]{87, 69, 66, 80}, 8); // index 8 to 11 must be 'WEBP'
    }

    /**
     * Check if a given byte array has a signature that starts with 'RIFF'
     *
     * @param data the byte array to check
     * @return true if the byte array starts with RIFF else false
     */
    public static boolean checkForRIFF(byte[] data) {
        return startsWith(data, new byte[]{82, 73, 70, 70}); // starts with 'RIFF'
    }


    /**
     * Check if a byte array starts with the same bytes as another byte array
     * that is however smaller than the first one.
     *
     * @param haystack e.g. {-1, -40, -1, -32, ........}
     * @param needle   e.g. {-1, -40, -1, -32}
     * @return true if {@code haystack} starts with the same bytes as {@code needle}
     */
    public static boolean startsWith(byte[] haystack, byte[] needle) {
        return containsAt(haystack, needle, 0);
    }


    /**
     * Check if a byte array contains another byte array that is equal or smaller in size
     *
     * @param haystack e.g. {..., -1, -40, -1, -32, ...}
     * @param needle   e.g. {-1, -40, -1, -32}
     * @param offset   the index where {@code needle} should be starting in {@code haystack}
     * @return true if {@code needle} is contained in {@code haystack} starting at index {@code offset}
     */
    public static boolean containsAt(byte[] haystack, byte[] needle, int offset) {
        if (needle.length < 1) {
            return true; // everything starts with empty
        }
        boolean haystackEmpty = haystack.length == 0;
        boolean offsetOutOfRange = (offset < 0 || offset >= haystack.length);
        boolean needleDoesNotFit = (haystack.length < needle.length + offset);
        if (haystackEmpty || offsetOutOfRange || needleDoesNotFit) {
            return false;
        }

        for (int i = 0; i < needle.length; i++) {
            if (needle[i] != haystack[offset + i]) {
                return false;
            }
        }
        return true;
    }


}