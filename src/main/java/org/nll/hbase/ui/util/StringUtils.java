package org.nll.hbase.ui.util;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Lists;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {

    private static final Logger logger = LoggerFactory
            .getLogger(StringUtils.class);
    /**
     * The empty String <code>""</code>.
     *
     * @since 2.0
     */
    public static final String EMPTY = "";
    /**
     * Represents a failed index search.
     *
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;
    private static final ImmutableBiMap<String, String> xml_charts1 = ImmutableBiMap
            .of("{", "\\{", "}", "\\}", "(", "\\(", ")", "\\)", "[", "\\[");
    private static final ImmutableBiMap<String, String> xml_charts2 = ImmutableBiMap
            .of("]", "\\]", ".", "\\.", "?", "\\?", "$", "\\$", "^", "\\^");
    private static final ImmutableBiMap<String, String> xml_charts3 = ImmutableBiMap
            .of("+", "\\+", "-", "\\-", "|", "\\|", "&", "&amp;");
    // 日期正则
    private final static String strictDatePattern = "\\d{2,4}[-年/\\s]*\\d{1,2}[-月/\\s]*\\d{1,2}[\\s日/]*(\\d{1,2}:\\d{1,2}(:\\d{1,2})?)?";

    private static Pattern strictDateRegexPattern = Pattern.compile(
            strictDatePattern, Pattern.CANON_EQ | Pattern.DOTALL
            | Pattern.CASE_INSENSITIVE);
    private static final Set<String> IRRELEVANT_PARAMETERS = new HashSet<String>(
            3);

    static {
        IRRELEVANT_PARAMETERS.add("jsessionid");
        IRRELEVANT_PARAMETERS.add("phpsessid");
        IRRELEVANT_PARAMETERS.add("aspsessionid");
    }

    public static boolean isUrl(String url) {
        if (url == null || url.length() == 0) {
            return false;
        }
        // NOTE:为了支持参数化的url，故暂时不用正则的方式来严格的校验
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static boolean isNullOrEmpty(String content) {
        return content == null || content.length() == 0
                || "null".equals(content);
    }

    public static boolean isNotNullOrEmpty(String content) {
        return (content != null) && (content.length() > 0);
    }

    public static String defaultIfNullOrEmpty(String str, String defaultStr) {
        return StringUtils.isNullOrEmpty(str) ? defaultStr : str;
    }

    /**
     * 获取数字字符.
     *
     * @param str
     * @return
     */
    public static String getNumStr(String str) {
        if (isNullOrEmpty(str)) {
            return "";
        }
        Pattern numPattern = Pattern.compile("\\d");
        Matcher matcher = numPattern.matcher(str);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            builder.append(matcher.group());
        }
        return builder.toString();
    }

    private static HashFunction hashFunction = Hashing.md5();

    /**
     * 求一个字符串的MD5,如果为空返回""
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        if (isNullOrEmpty(str)) {
            return "";
        }
        return hashFunction.hashString(str).toString();
    }

    /**
     * 获取最后一个字符,如果为空返回"";
     *
     * @param str
     * @return
     */
    public static String last(String str) {
        if (StringUtils.isNotNullOrEmpty(str)) {
            return str.substring(str.length() - 1, str.length());
        } else {
            return "";
        }
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Checks if a CharSequence is empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>
     * NOTE: This method changed in Lang version 2.0. It no longer trims the
     * CharSequence. That functionality is available in isBlank().
     * </p>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     * @since 3.0 Changed signature from isEmpty(String) to
     * isEmpty(CharSequence)
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * <p>
     * Checks if a String is whitespace, empty ("") or null.
     * </p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param str the String to check, may be null
     * @return <code>true</code> if the String is null, empty or whitespace
     * @since 2.0
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Capitalizes a String changing the first letter to title case as per
     * {@link Character#toTitleCase(char)}. No other letters are changed.
     * </p>
     *
     * <p>
     * For a word based algorithm, see {@link WordUtils#capitalize(String)}. A
     * <code>null</code> input String returns <code>null</code>.
     * </p>
     *
     * <pre>
     * StringUtils.capitalize(null)  = null
     * StringUtils.capitalize("")    = ""
     * StringUtils.capitalize("cat") = "Cat"
     * StringUtils.capitalize("cAt") = "CAt"
     * </pre>
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, <code>null</code> if null String input
     * @see WordUtils#capitalize(String)
     * @see #uncapitalize(String)
     * @since 2.0
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toTitleCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Gets the substring before the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBefore(null, *)      = null
     * StringUtils.substringBefore("", *)        = ""
     * StringUtils.substringBefore("abc", "a")   = ""
     * StringUtils.substringBefore("abcba", "b") = "a"
     * StringUtils.substringBefore("abc", "c")   = "ab"
     * StringUtils.substringBefore("abc", "d")   = "abc"
     * StringUtils.substringBefore("abc", "")    = ""
     * StringUtils.substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 截取指定字符的前一个字符.
     *
     * @param str
     * @param separator
     * @return
     */
    public static String substringBeforeLastChar(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        if (pos == 0) {
            return "";
        }
        return str.substring(pos - 1, pos);
    }

    /**
     * <p>
     * Gets the substring after the first occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. A {@code null} separator will
     * return the empty string if the input string is not {@code null}.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfter(null, *)      = null
     * StringUtils.substringAfter("", *)        = ""
     * StringUtils.substringAfter(*, null)      = ""
     * StringUtils.substringAfter("abc", "a")   = "bc"
     * StringUtils.substringAfter("abcba", "b") = "cba"
     * StringUtils.substringAfter("abc", "c")   = ""
     * StringUtils.substringAfter("abc", "d")   = ""
     * StringUtils.substringAfter("abc", "")    = "abc"
     * </pre>
     *
     * @param str the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the first occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the substring before the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the input string.
     * </p>
     *
     * <p>
     * If nothing is found, the string input is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringBeforeLast(null, *)      = null
     * StringUtils.substringBeforeLast("", *)        = ""
     * StringUtils.substringBeforeLast("abcba", "b") = "abc"
     * StringUtils.substringBeforeLast("abc", "c")   = "ab"
     * StringUtils.substringBeforeLast("a", "a")     = ""
     * StringUtils.substringBeforeLast("a", "z")     = "a"
     * StringUtils.substringBeforeLast("a", null)    = "a"
     * StringUtils.substringBeforeLast("a", "")      = "a"
     * </pre>
     *
     * @param str the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring before the last occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringBeforeLast(String str, String separator) {
        if (isEmpty(str) || isEmpty(separator)) {
            return str;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * <p>
     * Gets the substring after the last occurrence of a separator. The
     * separator is not returned.
     * </p>
     *
     * <p>
     * A {@code null} string input will return {@code null}. An empty ("")
     * string input will return the empty string. An empty or {@code null}
     * separator will return the empty string if the input string is not
     * {@code null}.
     * </p>
     *
     * <p>
     * If nothing is found, the empty string is returned.
     * </p>
     *
     * <pre>
     * StringUtils.substringAfterLast(null, *)      = null
     * StringUtils.substringAfterLast("", *)        = ""
     * StringUtils.substringAfterLast(*, "")        = ""
     * StringUtils.substringAfterLast(*, null)      = ""
     * StringUtils.substringAfterLast("abc", "a")   = "bc"
     * StringUtils.substringAfterLast("abcba", "b") = "a"
     * StringUtils.substringAfterLast("abc", "c")   = ""
     * StringUtils.substringAfterLast("a", "a")     = ""
     * StringUtils.substringAfterLast("a", "z")     = ""
     * </pre>
     *
     * @param str the String to get a substring from, may be null
     * @param separator the String to search for, may be null
     * @return the substring after the last occurrence of the separator,
     * {@code null} if null String input
     * @since 2.0
     */
    public static String substringAfterLast(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (isEmpty(separator)) {
            return EMPTY;
        }
        int pos = str.lastIndexOf(separator);
        if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * <p>
     * Gets the String that is nested in between two Strings. Only the first
     * match is returned.
     * </p>
     *
     * <p>
     * A <code>null</code> input String returns <code>null</code>. A
     * <code>null</code> open/close returns <code>null</code> (no match). An
     * empty ("") open and close returns an empty string.
     * </p>
     *
     * <pre>
     * StringUtils.substringBetween("wx[b]yz", "[", "]") = "b"
     * StringUtils.substringBetween(null, *, *)          = null
     * StringUtils.substringBetween(*, null, *)          = null
     * StringUtils.substringBetween(*, *, null)          = null
     * StringUtils.substringBetween("", "", "")          = ""
     * StringUtils.substringBetween("", "", "]")         = null
     * StringUtils.substringBetween("", "[", "]")        = null
     * StringUtils.substringBetween("yabcz", "", "")     = ""
     * StringUtils.substringBetween("yabcz", "y", "z")   = "abc"
     * StringUtils.substringBetween("yabczyabcz", "y", "z")   = "abc"
     * </pre>
     *
     * @param str the String containing the substring, may be null
     * @param open the String before the substring, may be null
     * @param close the String after the substring, may be null
     * @return the substring, <code>null</code> if no match
     * @since 2.0
     */
    public static String substringBetween(String str, String open, String close) {
        if (str == null || open == null || close == null) {
            return null;
        }
        int start = str.indexOf(open);
        if (start != INDEX_NOT_FOUND) {
            int end = str.indexOf(close, start + open.length());
            if (end != INDEX_NOT_FOUND) {
                return str.substring(start + open.length(), end);
            }
        }
        return null;
    }

    /**
     * 置空字符串
     *
     * @param content 判断的字符串
     * @return 如果字符串为空，返回"",否则返回原文
     */
    public static String empty(String content) {
        return StringUtils.isNullOrEmpty(content) ? "" : content;
    }

    /**
     * 支持的格式串：yyyy,yy,MM,M,dd,d ,其中:
     * yyyy（四位年），yy（两位年），MM（两位月，如果不足两位，前导0补齐），M（月，不补齐），dd(两位日，如果不足两位，前导0补齐)，d(日，
     * 不补齐)
     *
     * @param oldUrl
     * @return
     */
    public static String preProcessUrl(String oldUrl) {
        //
        if (oldUrl.indexOf('{') >= 0) {
            String tempUrl = oldUrl.replace("{", "{0,date,");
            return MessageFormat.format(tempUrl, new Date());
        }
        return oldUrl;
    }

    public static Date normalizeStr2Date(String before) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String afterDateString = normalizeDate(before);
        return formatter.parse(afterDateString);
    }

    /**
     * normalization date string,<br>
     * not support two-digit year,not support time-only string
     *
     * @return normalized date string,format is:yyyy-MM-dd HH:mm:ss
     */
    public static String normalizeDate(String before) {

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (StringUtils.isNullOrEmpty(before)) {
            return formatter.format(new java.util.Date());
        }
        int[] calendarField = new int[]{Calendar.YEAR, Calendar.MONTH,
            Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE,
            Calendar.SECOND};
        Calendar calendar = Calendar.getInstance();
        try {
            Matcher matcher = strictDateRegexPattern.matcher(before);
            if (matcher.find()) {
                before = matcher.group();
            }

            String[] tmp = before.replaceAll("\\D", " ").split("\\s+");
            int i = 0, j = 0;
            if (tmp != null && tmp.length > 0 && "".equals(tmp[0])) {
                i = 1;
            }
            if (Integer.parseInt(tmp[i]) < 13) {
                j = 1;
            }
            for (; i < tmp.length; i++, j++) {
                if (j == 1) {
                    calendar.set(calendarField[j], Integer.parseInt(tmp[i]) - 1);
                } else {
                    calendar.set(calendarField[j], Integer.parseInt(tmp[i]));
                }
            }
            for (; j < calendarField.length; j++) {
                calendar.set(calendarField[j], 0);
            }
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            logger.error("日期转换错误,内容:(" + before.length() + ")" + before);
            return formatter.format(new Date());
        }
    }

    /**
     * 转换为int型，如果不能转换，则返回-1
     *
     * @param value
     * @return
     */
    public static int tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 转换为int型，如果不能转换，则返回defaultValue
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 转换为Date型，如果不能转换，则返回now
     *
     * @param value
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date tryParseDate(String value) {
        try {
            return new Date(Date.parse(value));
        } catch (Exception e) {
            return new Date();
        }
    }

    /**
     * 转换为Date型，如果不能转换，则返回defaultValue
     *
     * @param value
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Date tryParseDate(String value, Date defaultValue) {
        try {
            return new Date(Date.parse(value));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * 解析http头中包含的日期
     *
     * @param value
     * @return
     */
    public static Date parseHttpDate(String value) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss", Locale.US);
            Date expires = simpleDateFormat.parse(value);
            return expires;
        } catch (Exception e) {
            return tryParseDate(value);
        }
    }

    /**
     * 批量格式化url，通过相对url获取绝对url
     *
     * @param baseUrl
     * @param urlList
     * @return
     */
    public static List<String> normalizeUrl(String baseUrl, List<String> urlList) {
        List<String> newUrlList = Lists.newArrayList();
        for (String url : urlList) {
            newUrlList.add(normalizeUrl(baseUrl, url));
        }
        return newUrlList;
    }

    /**
     * 批量格式化url，通过相对url获取绝对url
     *
     * @param baseUrl
     * @param urlList
     * @param urlSuffix url后缀
     * @return
     */
    public static List<String> normalizeUrl(String baseUrl,
            List<String> urlList, String urlSuffix) {
        List<String> newUrlList = Lists.newArrayList();
        for (String url : urlList) {
            newUrlList.add(normalizeUrl(baseUrl, url) + urlSuffix);
        }
        return newUrlList;
    }

    /**
     * 格式化url，通过相对url获取绝对url
     *
     * @param baseUrl 相对地址所在地址
     * @param url 相对地址
     * @return
     */
    public static String normalizeUrl(String baseUrl, String urlString) {
        URL base;
        try {
            try {
                base = new URL(baseUrl);
            } catch (MalformedURLException e) {
                // the base is unsuitable, but the attribute may be abs on its
                // own, so try that
                URL abs = new URL(urlString);
                return abs.toExternalForm();
            }
            // workaround: java resolves '//path/file + ?foo' to '//path/?foo',
            // not '//path/file?foo' as desired
            if (urlString.startsWith("?")) {
                urlString = base.getPath() + urlString;
            }
            URL abs = new URL(base, urlString);
            return abs.toExternalForm();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    /**
     * Takes a query string, separates the constituent name-value pairs, and
     * stores them in a SortedMap ordered by lexicographical order.
     *
     * @param queryString the query string
     * @return Null if there is no query string.
     */
    private static SortedMap<String, String> createParameterMap(
            final String queryString) {
        if (queryString == null || queryString.isEmpty()) {
            return null;
        }

        final String[] pairs = queryString.split("&");
        final SortedMap<String, String> params = new TreeMap<String, String>();

        for (final String pair : pairs) {
            if (pair.length() == 0) {
                continue;
            }

            String[] tokens = pair.split("=", 2);
            switch (tokens.length) {
                case 1:
                    if (pair.charAt(0) == '=') {
                        params.put("", tokens[0]);
                    } else {
                        params.put(tokens[0], "");
                    }
                    break;
                case 2:
                    params.put(tokens[0], tokens[1]);
                    break;
            }
        }
        return params;
    }

    /**
     * Canonicalize the query string.
     *
     * @param sortedParamMap Parameter name-value pairs in lexicographical
     * order.
     * @return Canonical form of query string.
     */
    private static String canonicalize(
            final SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap == null || sortedParamMap.isEmpty()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder(100);
        for (Map.Entry<String, String> pair : sortedParamMap.entrySet()) {
            final String key = pair.getKey().toLowerCase();
            // Ignore irrelevant parameters
            if (IRRELEVANT_PARAMETERS.contains(key) || key.startsWith("utm_")) {
                continue;
            }
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(percentEncodeRfc3986(pair.getKey()));
            if (!pair.getValue().isEmpty()) {
                sb.append('=');
                sb.append(percentEncodeRfc3986(pair.getValue()));
            }
        }
        return sb.toString();
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java
     * URLEncoder does not encode according to the RFC, so we make the extra
     * replacements.
     *
     * @param string Decoded string.
     * @return Encoded string per RFC 3986.
     */
    private static String percentEncodeRfc3986(String string) {
        try {
            string = string.replace("+", "%2B");
            string = URLDecoder.decode(string, "UTF-8");
            string = URLEncoder.encode(string, "UTF-8");
            return string.replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (Exception e) {
            return string;
        }
    }

    /**
     * Normalize path.
     *
     * @param path the path
     * @return the string
     */
    private static String normalizePath(final String path) {
        return path.replace("%7E", "~").replace(" ", "%20");
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterable} into a single String
     * containing the provided elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the iteration are represented by empty strings.
     * </p>
     *
     * <p>
     * See the examples here: {@link #join(Object[],char)}.
     * </p>
     *
     * @param iterable the {@code Iterable} providing the values to join
     * together, may be null
     * @param separator the separator character to use
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * <p>
     * Joins the elements of the provided {@code Iterable} into a single String
     * containing the provided elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String ("").
     * </p>
     *
     * <p>
     * See the examples here: {@link #join(Object[],String)}.
     * </p>
     *
     * @param iterable the {@code Iterable} providing the values to join
     * together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null iterator input
     * @since 2.3
     */
    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the array are represented by empty strings.
     * </p>
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @param separator the separator character to use
     * @return the joined String, {@code null} if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }

        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. Null objects or empty
     * strings within the array are represented by empty strings.
     * </p>
     *
     * <pre>
     * StringUtils.join(null, *)               = null
     * StringUtils.join([], *)                 = ""
     * StringUtils.join([null], *)             = ""
     * StringUtils.join(["a", "b", "c"], ';')  = "a;b;c"
     * StringUtils.join(["a", "b", "c"], null) = "abc"
     * StringUtils.join([null, "", "a"], ';')  = ";;a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @param separator the separator character to use
     * @param startIndex the first index to start joining from. It is an error
     * to pass in an end index past the end of the array
     * @param endIndex the index to stop joining from (exclusive). It is an
     * error to pass in an end index past the end of the array
     * @return the joined String, {@code null} if null array input
     * @since 2.0
     */
    public static String join(Object[] array, char separator, int startIndex,
            int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String (""). Null objects or empty strings within
     * the array are represented by empty strings.
     * </p>
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @return the joined String, {@code null} if null array input
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     *
     * <p>
     * No delimiter is added before or after the list. A {@code null} separator
     * is the same as an empty String (""). Null objects or empty strings within
     * the array are represented by empty strings.
     * </p>
     *
     * <pre>
     * StringUtils.join(null, *)                = null
     * StringUtils.join([], *)                  = ""
     * StringUtils.join([null], *)              = ""
     * StringUtils.join(["a", "b", "c"], "--")  = "a--b--c"
     * StringUtils.join(["a", "b", "c"], null)  = "abc"
     * StringUtils.join(["a", "b", "c"], "")    = "abc"
     * StringUtils.join([null, "", "a"], ',')   = ",,a"
     * </pre>
     *
     * @param array the array of values to join together, may be null
     * @param separator the separator character to use, null treated as ""
     * @param startIndex the first index to start joining from. It is an error
     * to pass in an end index past the end of the array
     * @param endIndex the index to stop joining from (exclusive). It is an
     * error to pass in an end index past the end of the array
     * @return the joined String, {@code null} if null array input
     */
    public static String join(Object[] array, String separator, int startIndex,
            int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = EMPTY;
        }

        // endIndex - startIndex > 0: Len = NofStrings *(len(firstString) +
        // len(separator))
        // (Assuming that all Strings are roughly equally long)
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return EMPTY;
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    // -----------------------------------------------------------------------
    /**
     * <p>
     * Joins the elements of the provided array into a single String containing
     * the provided list of elements.
     * </p>
     *
     * <p>
     * No separator is added to the joined String. Null objects or empty strings
     * within the array are represented by empty strings.
     * </p>
     *
     * <pre>
     * StringUtils.join(null)            = null
     * StringUtils.join([])              = ""
     * StringUtils.join([null])          = ""
     * StringUtils.join(["a", "b", "c"]) = "abc"
     * StringUtils.join([null, "", "a"]) = "a"
     * </pre>
     *
     * @param <T> the specific type of values to join together
     * @param elements the values to join together, may be null
     * @return the joined String, {@code null} if null array input
     * @since 2.0
     * @since 3.0 Changed signature to use varargs
     */
    @SuppressWarnings("unchecked")
    public static <T> String join(T... elements) {
        return join(elements, null);
    }

    public static String escapeRegularRegex(String regex) {
        String temp = regex;
        // 需要转义的字符.$^{}[]()|*+?\-
        temp = temp.replaceAll("\\.", "@@杠@@点@@");
        temp = temp.replaceAll("\\^", "@@杠@@弯@@");
        temp = temp.replaceAll("\\{", "@@杠@@左花括号@@");
        temp = temp.replaceAll("\\}", "@@杠@@右花括号@@");
        temp = temp.replaceAll("\\[", "@@杠@@左方括号@@");
        temp = temp.replaceAll("\\]", "@@杠@@右方括号@@");
        temp = temp.replaceAll("\\(", "@@杠@@左圆括号@@");
        temp = temp.replaceAll("\\)", "@@杠@@右圆括号@@");
        temp = temp.replaceAll("\\|", "@@杠@@竖杠@@");
        temp = temp.replaceAll("\\*", "@@杠@@星号@@");
        temp = temp.replaceAll("\\+", "@@杠@@加号@@");
        temp = temp.replaceAll("\\?", "@@杠@@问号@@");
        temp = temp.replaceAll("\\\\", "@@杠@@邪杠@@");
        temp = temp.replaceAll("\\-", "@@杠@@减号@@");

        temp = temp.replaceAll("\\.", "\\.");
        temp = temp.replaceAll("\\$", "\\$");
        temp = temp.replaceAll("\\^", "\\^");
        temp = temp.replaceAll("\\{", "\\{");
        temp = temp.replaceAll("\\}", "\\}");
        temp = temp.replaceAll("\\[", "\\[");
        temp = temp.replaceAll("\\]", "\\]");
        temp = temp.replaceAll("\\(", "\\(");
        temp = temp.replaceAll("\\)", "\\)");
        temp = temp.replaceAll("\\|", "\\|");
        temp = temp.replaceAll("\\*", "\\*");
        temp = temp.replaceAll("\\+", "\\+");
        temp = temp.replaceAll("\\?", "\\?");
        temp = temp.replaceAll("\\\\", "\\\\");
        temp = temp.replaceAll("\\-", "\\-");

        temp = temp.replaceAll("@@杠@@点@@", "\\\\.");
        temp = temp.replaceAll("@@杠@@弯@@", "\\\\^");
        temp = temp.replaceAll("@@杠@@左花括号@@", "\\{");
        temp = temp.replaceAll("@@杠@@右花括号@@", "\\\\}");
        temp = temp.replaceAll("@@杠@@左方括号@@", "\\\\[");
        temp = temp.replaceAll("@@杠@@右方括号@@", "\\\\]");
        temp = temp.replaceAll("@@杠@@左圆括号@@", "\\\\(");
        temp = temp.replaceAll("@@杠@@右圆括号@@", "\\\\)");
        temp = temp.replaceAll("@@杠@@竖杠@@", "\\\\|");
        temp = temp.replaceAll("@@杠@@星号@@", "\\\\*");
        temp = temp.replaceAll("@@杠@@加号@@", "\\\\+");
        temp = temp.replaceAll("@@杠@@问号@@", "\\\\?");
        temp = temp.replaceAll("@@杠@@邪杠@@", "\\\\\\\\");
        temp = temp.replaceAll("@@杠@@减号@@", "\\\\-");

        return temp;
    }

    public static String coverXml(String xml) {
        for (Entry<String, String> double_char : xml_charts1.entrySet()) {
            xml = xml.replace(double_char.getKey(), double_char.getValue());
        }
        for (Entry<String, String> double_char : xml_charts2.entrySet()) {
            xml = xml.replace(double_char.getKey(), double_char.getValue());
        }
        for (Entry<String, String> double_char : xml_charts3.entrySet()) {
            xml = xml.replace(double_char.getKey(), double_char.getValue());
        }
        return xml;
    }

    public static String reCoverXml(String xml) {
        for (Entry<String, String> double_char : xml_charts1.entrySet()) {
            xml = xml.replace(double_char.getValue(), double_char.getKey());
        }
        for (Entry<String, String> double_char : xml_charts2.entrySet()) {
            xml = xml.replace(double_char.getValue(), double_char.getKey());
        }
        for (Entry<String, String> double_char : xml_charts3.entrySet()) {
            xml = xml.replace(double_char.getValue(), double_char.getKey());
        }
        return xml;
    }

    /**
     * 转换xml中的"&lt;"和"&gt;"为< >
     *
     * @param source
     * @return
     */
    public static String coverXMLTag(String source) {
        source = source.replace("&lt;", "<");
        source = source.replace("&gt;", ">");
        return source;
    }

    /**
     * 获取html源代码中的body部分.
     * <p>
     * 如果没有body标签，那么返回全部
     *
     * @param html
     * @return
     */
    public static String body(String html) {
        int start_index = html.toLowerCase().indexOf("<body");
        int end_index = html.toLowerCase().lastIndexOf("</body>");
        if (start_index < 0) {
            start_index = 0;
        }
        if (end_index < 0) {
            end_index = html.length();
        }
        return html.substring(start_index, end_index);
    }

    /**
     * <p>
     * Find the Levenshtein distance between two Strings.
     * </p>
     *
     * <p>
     * This is the number of changes needed to change one String into another,
     * where each change is a single character modification (deletion, insertion
     * or substitution).
     * </p>
     *
     * <p>
     * The previous implementation of the Levenshtein distance algorithm was
     * from <a
     * href="http://www.merriampark.com/ld.htm">http://www.merriampark.com
     * /ld.htm</a>
     * </p>
     *
     * <p>
     * Chas Emerick has written an implementation in Java, which avoids an
     * OutOfMemoryError which can occur when my Java implementation is used with
     * very large strings.<br>
     * This implementation of the Levenshtein distance algorithm is from <a
     * href="http://www.merriampark.com/ldjava.htm">http://www.merriampark.com/
     * ldjava.htm</a>
     * </p>
     *
     * <pre>
     * StringUtils.getLevenshteinDistance(null, *)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance(*, null)             = IllegalArgumentException
     * StringUtils.getLevenshteinDistance("","")               = 0
     * StringUtils.getLevenshteinDistance("","a")              = 1
     * StringUtils.getLevenshteinDistance("aaapppp", "")       = 7
     * StringUtils.getLevenshteinDistance("frog", "fog")       = 1
     * StringUtils.getLevenshteinDistance("fly", "ant")        = 3
     * StringUtils.getLevenshteinDistance("elephant", "hippo") = 7
     * StringUtils.getLevenshteinDistance("hippo", "elephant") = 7
     * StringUtils.getLevenshteinDistance("hippo", "zzzzzzzz") = 8
     * StringUtils.getLevenshteinDistance("hello", "hallo")    = 1
     * </pre>
     *
     * @param s the first String, must not be null
     * @param t the second String, must not be null
     * @return result distance
     * @throws IllegalArgumentException if either String input {@code null}
     * @since 3.0 Changed signature from getLevenshteinDistance(String, String)
     * to getLevenshteinDistance(CharSequence, CharSequence)
     */
    public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
        if (s == null || t == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        /*
         * The difference between this impl. and the previous is that, rather
         * than creating and retaining a matrix of size s.length() + 1 by
         * t.length() + 1, we maintain two single-dimensional arrays of length
         * s.length() + 1. The first, d, is the 'current working' distance array
         * that maintains the newest distance cost counts as we iterate through
         * the characters of String s. Each time we increment the index of
         * String t we are comparing, d is copied to p, the second int[]. Doing
         * so allows us to retain the previous cost counts as required by the
         * algorithm (taking the minimum of the cost count to the left, up one,
         * and diagonally up and to the left of the current cost count being
         * calculated). (Note that the arrays aren't really copied anymore, just
         * switched...this is clearly much better than cloning an array or doing
         * a System.arraycopy() each time through the outer loop.)
         *
         * Effectively, the difference between the two implementations is this
         * one does not cause an out of memory condition when calculating the LD
         * over two very large strings.
         */
        int n = s.length(); // length of s
        int m = t.length(); // length of t

        if (n == 0) {
            return m;
        } else if (m == 0) {
            return n;
        }

        if (n > m) {
            // swap the input strings to consume less memory
            CharSequence tmp = s;
            s = t;
            t = tmp;
            n = m;
            m = t.length();
        }

        int p[] = new int[n + 1]; // 'previous' cost array, horizontally
        int d[] = new int[n + 1]; // cost array, horizontally
        int _d[]; // placeholder to assist in swapping p and d

        // indexes into strings s and t
        int i; // iterates through s
        int j; // iterates through t

        char t_j; // jth character of t

        int cost; // cost

        for (i = 0; i <= n; i++) {
            p[i] = i;
        }

        for (j = 1; j <= m; j++) {
            t_j = t.charAt(j - 1);
            d[0] = j;

            for (i = 1; i <= n; i++) {
                cost = s.charAt(i - 1) == t_j ? 0 : 1;
                // minimum of cell to the left+1, to the top+1, diagonally left
                // and up +cost
                d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1]
                        + cost);
            }

            // copy current distance counts to 'previous row' distance counts
            _d = p;
            p = d;
            d = _d;
        }

        // our last action in the above loop was to switch d and p, so p now
        // actually has the most recent cost counts
        return p[n];
    }

}
