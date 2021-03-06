package ui;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import tool.PruningStrategy;


/**
 * Command-line options.
 * @author thiago
 *
 */
class Options {
    private String featureModelFilePath;
    private String umlModelsFilePath;
    private String paramPath;
    private String configuration;
    private String configurationsFilePath;
    private boolean printAllConfigurations;
    private boolean statsEnabled;
    private PruningStrategy pruningStrategy;

    static Options parseOptions(String[] args) throws IOException {
        OptionParser optionParser = new OptionParser();
        OptionSpec<String> featureModelOption = optionParser
                .accepts("feature-model")
                .withRequiredArg()
                .defaultsTo("fm.txt")
                .describedAs("File");
        OptionSpec<String> umlModelsOption = optionParser
                .accepts("uml-models")
                .withRequiredArg()
                .defaultsTo("modeling.xml")
                .describedAs("File");
        OptionSpec<String> paramPathOption = optionParser
                .accepts("param-path")
                .withRequiredArg()
                .defaultsTo("/opt/param-2-3-64")
                .describedAs("Directory");

        OptionSpec<String> configurationsFileOption = optionParser
                .accepts("configurations-file")
                .withRequiredArg()
                .defaultsTo("configurations.txt")
                .describedAs("File");
        OptionSpec<String> configurationOption = optionParser
                .accepts("configuration")
                .withRequiredArg();
        OptionSpec<Void> allConfigurationsOption = optionParser
                .accepts("all-configurations",
                         "Print the reliabilities of all valid configurations");
        OptionSpec<Void> statsEnabledOption = optionParser
                .accepts("stats",
                         "Print profiling stats");

        OptionSpec<PruningStrategy> pruningStrategyOption = optionParser
                .accepts("pruning-strategy",
                         "The strategy that should be used for pruning invalid configurations. Can be one of: FM (whole feature model); NONE (no pruning)")
                .withRequiredArg()
                .ofType(PruningStrategy.class)
                .defaultsTo(PruningStrategy.FM)
                .describedAs("FM | NONE");

        OptionSpec<Void> helpOption = optionParser
                .accepts("help")
                .forHelp();

        OptionSet options = optionParser.parse(args);
        if (options.has(helpOption)) {
            optionParser.printHelpOn(System.out);
            System.exit(1);
        }

        Options result = new Options();
        result.featureModelFilePath = options.valueOf(featureModelOption);
        result.umlModelsFilePath = options.valueOf(umlModelsOption);
        result.paramPath = options.valueOf(paramPathOption);
        result.configuration = options.valueOf(configurationOption);
        result.configurationsFilePath = options.valueOf(configurationsFileOption);
        result.printAllConfigurations = options.has(allConfigurationsOption);
        result.statsEnabled = options.has(statsEnabledOption);
        result.pruningStrategy = options.valueOf(pruningStrategyOption);

        return result;
    }

    public String getFeatureModelFilePath() {
        return featureModelFilePath;
    }

    public String getUmlModelsFilePath() {
        return umlModelsFilePath;
    }

    public String getParamPath() {
        return paramPath;
    }

    public boolean hasStatsEnabled() {
        return statsEnabled;
    }

    public boolean hasPrintAllConfigurations() {
        return printAllConfigurations;
    }

    public String getConfiguration() {
        return configuration;
    }

    public String getConfigurationsFilePath() {
        return configurationsFilePath;
    }

    public PruningStrategy getPruningStrategy() {
        return pruningStrategy;
    }

}
