package kalang.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import kalang.compiler.Configuration;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Kason Yang
 */
public abstract class ShellBase {

    protected final String appName,syntax;
    
    protected final Options options;

    public ShellBase(String appName,String syntax, Options options) {
        this.appName = appName;
        this.options = options;
        this.syntax = syntax;
        options.addOption("h", "help", false, "show this help message");
        options.addOption("v", "verbose", false, "show verbose information");
        options.addOption(null, "script-base", true, "specify default script base class");
        options.addOption("l", "libpath", true, "library path");
        options.addOption(null,"classpath", true, "compile classpath");
        options.addOption("s","sourcepath", true, "source directory");
        options.addOption("v","version",false,"show version information");
    }
    
    public void run(String[] args) {
        DefaultParser parser = new DefaultParser();
        CommandLine cli;
        try {
            cli = parser.parse(options, args, true);
        } catch (ParseException ex) {
            System.err.println(ex.getMessage());
            return;
        }
        if (cli == null || cli.hasOption("help")) {
            printUsage();
        } else if (cli.hasOption("version")) {
            printVersion();
        } else {
            execute(cli);
        }
    }
    
    protected abstract void execute(CommandLine cli);
    
    protected void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(syntax, options);
    }
    
    protected void printVersion() {
        Properties prop = new Properties();
        InputStream is = this.getClass().getResourceAsStream("/default.properties");
        if (is != null) {
            try {
                prop.load(is);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        String version = prop.getProperty("version", "UNKNOWN");
        String buildtime = prop.getProperty("buildtime", "UNKNOWN");
        System.out.println(String.format("%s %s\nBuild time : %s", appName, version, buildtime));
    }

    protected Configuration createConfiguration(CommandLine cli) {
        String baseScriptClass = cli.getOptionValue("script-base", "");
        Configuration config = new Configuration();
        if (!baseScriptClass.isEmpty()) {
            config.setScriptBaseClass(baseScriptClass);
        }
        return config;
    }

    protected ClassLoader createClassLoader(CommandLine cli) {
        boolean verbose = cli.hasOption("verbose");
        List<URL> urls = new LinkedList();
        String[] libPaths = cli.getOptionValue("libpath", "").split(";");
        for (String l : libPaths) {
            if (l.isEmpty()) {
                continue;
            }
            File ld = new File(l);
            if (!ld.isDirectory()) {
                continue;
            }
            Collection<File> jars = FileUtils.listFiles(ld, new String[]{"jar"}, false);
            for (File j : jars) {
                try {
                    urls.add(j.toURI().toURL());
                } catch (MalformedURLException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        }
        File[] cps = parseClassPath(cli);
        for (int i = 0; i < cps.length; i++) {
            try {
                urls.add(cps[i].toURI().toURL());
            } catch (MalformedURLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        if (verbose) {
            for (URL u : urls) {
                System.out.println("Add class path:" + u);
            }
        }
        return new URLClassLoader(urls.toArray(new URL[urls.size()]));
    }

    protected File[] parseClassPath(CommandLine cli) {
        if (cli.hasOption("classpath")) {
            String[] cps = cli.getOptionValue("classpath").split(";");
            File[] file = new File[cps.length];
            for (int i = 0; i < cps.length; i++) {
                file[i] = new File(cps[i]);
            }
            return file;
        }
        return new File[0];
    }

}