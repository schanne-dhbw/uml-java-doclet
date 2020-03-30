package info.leadinglight.umljavadoclet.printer;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle options specific to uml-java-doclet.
 */
public class DiagramOptions {
    public DiagramOptions() {
        addOption(LINETYPE, "polyline,spline,ortho", "ortho", 2);
        addOption(DEPENDENCIES, "public,protected,package,private", "public", 2);
        addOption(PACKAGE_ORIENTATION, "left-to-right,top-to-bottom", "top-to-bottom", 2);
    }

    public enum LineType { SPLINE, POLYLINE, ORTHO };
    public enum Visibility { PUBLIC, PROTECTED, PACKAGE, PRIVATE };
    public enum Orientation { LEFT_TO_RIGHT, TOP_TO_BOTTOM };

    // Options as enumerated types
    public LineType getLineType() {
        return LineType.valueOf(getOptionEnumValue(LINETYPE));
    }

    public Visibility getDependenciesVisibility() {
        return Visibility.valueOf(getOptionEnumValue(DEPENDENCIES));
    }

    public Orientation getPackageOrientation() {
        return Orientation.valueOf(getOptionEnumValue(PACKAGE_ORIENTATION));
    }

    private static final String LINETYPE = "linetype";
    private static final String DEPENDENCIES = "dependencies";
    private static final String PACKAGE_ORIENTATION = "package-orientation";

    /**
     * Set the options as provided in the strings.
     * Invalid options are ignored.
     * @param docletOptions Options provided in javadoc format for options: an array of string arrays, each
     * array indicating the name of the option (index 0) and the associated value.
     */
    public void set(String[][] docletOptions) {
        for (String[] docletOption : docletOptions) {
            DiagramOption option = getOptionForDocletName(docletOption[0]);
            if (option != null) {
                option.setValue(docletOption[1]);
            }
        }
    }

    /**
     * Check to see if the specified option is valid.
     * @param docletName Name of the option to check.
     * @return Whether or not the option is valid.
     */
    public boolean isValidOption(String docletName) {
        return getOptionForDocletName(docletName) != null;
    }

    /**
     * Get the number of parameters for the specified option.
     * @param docletName Name of option to get parameters for.
     * @return Number of parameters.
     */
    public int getOptionLength(String docletName) {
        DiagramOption option = getOptionForDocletName(docletName);
        return option != null ? option.getLength() : 0;
    }

    /**
     * Check to see if the specified setting is a valid option.
     * @param setting Setting to check.
     * @return Error associated with the option, null if no error.
     */
    public String checkOption(String[] setting) {
        String docletName = setting[0];
        DiagramOption option = getOptionForDocletName(docletName);
        if (option == null) {
            return "Invalid option " + docletName;
        }
        String value = setting[1];
        if (!option.isValidValue(value)) {
            return "Invalid value " + value + " for option " + docletName + "; valid values are " + option.getValidValues();
        }
        return null;
    }

    // Helpers

    private void addOption(String name, String validValues, String defaultValue, int length) {
        DiagramOption option = new DiagramOption(name, validValues, defaultValue, length);
        options.add(option);
    }

    private DiagramOption getOption(String name) {
        for (DiagramOption option: options) {
            if (option.getName().equals(name)) {
                return option;
            }
        }
        return null;
    }

    private DiagramOption getOptionForDocletName(String nameWithHyphen) {
        String name = nameWithHyphen.substring(1);
        return getOption(name);
    }

    private String getOptionValue(String name) {
        DiagramOption option = getOption(name);
        return option != null ? option.getValue() : null;
    }

    private void setOptionValue(String name, String value) {
        DiagramOption option = getOption(name);
        option.setValue(value);
    }

    private String getOptionEnumValue(String name) {
        String value = getOptionValue(name);
        // Any hyphens in the name are treated as _ in the enum value.
        return value != null ? value.toUpperCase().replace("-", "_") : null;
    }

    @Override
    public String toString() {
        return "DiagramOptions{" +
            "options=" + options +
            '}';
    }

    private List<DiagramOption> options = new ArrayList<>();
}
