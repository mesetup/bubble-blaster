package com.ultreon.commons.crash;

import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public final class CrashReport extends CrashCategory {
    private final List<CrashCategory> categories = new ArrayList<>();

    public CrashReport(String details, CrashReport report) {
        super(details);

        throwable = addCrashReport(report).throwable;
    }

    public CrashReport(String details, @Nullable CrashReport report, Throwable t) {
        this(details, t);

        if (t instanceof GameCrash) this.throwable = addReportedException((GameCrash) t).throwable;
        if (report != null) addCrashReport(report);
    }

    public CrashReport(String details, Throwable t) {
        super(details, t);

        if (t instanceof GameCrash) {
            this.throwable = addReportedException((GameCrash) t).throwable;
        }
    }

    private CrashReport addCrashReport(CrashReport report) {
        CrashCategory cat = new CrashCategory(report.getDetails(), report.getThrowable());
        cat.entries.clear();
        cat.entries.addAll(report.entries);
        addCategory(cat);

        for (CrashCategory category : report.getCategories()) {
            addCategory(category);
        }

        return report;
    }

    private CrashReport addReportedException(GameCrash exception) {
        CrashReport crashReport = exception.getCrashReport();
        CrashReport crashReport1 = new CrashReport(crashReport.details, crashReport.throwable);
        crashReport1.categories.addAll(crashReport.categories.subList(0, crashReport.categories.size() - 1));
        crashReport1.entries.addAll(crashReport.entries);
        return addCrashReport(crashReport1);
    }

    @NotNull
    public Throwable getThrowable() {
        return throwable;
    }

    public void addCategory(CrashCategory crashCategory) {
        this.categories.add(crashCategory);
    }

    public List<CrashCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    private CrashReport getFinalForm() {
        CrashReport crashReport = new CrashReport(details, throwable);
        crashReport.categories.addAll(categories);
        crashReport.entries.addAll(entries);

        Runtime runtime = Runtime.getRuntime();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();

        CrashCategory category = new CrashCategory("System Details");
        category.add("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
//        category.add("CPU", hardwareAbstractionLayer.getProcessor().getProcessorIdentifier().getName());
//        category.add("GPU", hardwareAbstractionLayer.getGraphicsCards().get(0).getName());
        category.add("Memory", runtime.totalMemory() - runtime.freeMemory() + "/" + runtime.totalMemory());

        // Todo: implement.
//        category.add("Loaded Addons", AddonManager.getInstance().getContainers().stream()
//                .map(AddonContainer::getAddonId)
//                .collect(Collectors.joining(", ")));
//        category.add("Constructed Addons", AddonManager.getInstance().getContainers().stream()
//                .filter((container) -> container.getJavaAddon() != null)
//                .map(AddonContainer::getAddonId)
//                .collect(Collectors.joining(", ")));

        crashReport.addCategory(category);
        return crashReport;
    }

    public GameCrash getReportedException() {
        return new GameCrash(getFinalForm());
    }

    @Override
    public String toString() {
        String s1 = "// " + details + "\r\n";
        StringBuilder cs = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();

//        List<AbstractMap.SimpleEntry<String, String>> entries = this.entries;
//        entries.add(new AbstractMap.SimpleEntry<>("OS", System.getProperty("os.name")));
//        entries.add(new AbstractMap.SimpleEntry<>("Processor", hardwareAbstractionLayer.getProcessor().getProcessorIdentifier().getName()));
//        entries.add(new AbstractMap.SimpleEntry<>("GPU", hardwareAbstractionLayer.getGraphicsCards().get(0).getName()));
//        entries.add(new AbstractMap.SimpleEntry<>("Memory", runtime.totalMemory() - runtime.freeMemory() + "/" + runtime.totalMemory()));
//        entries.add(new AbstractMap.SimpleEntry<>("Loaded Addons", AddonManager.getInstance().getContainers().stream()
//                .map(AddonContainer::getAddonId)
//                .collect(Collectors.joining(", "))));
//        entries.add(new AbstractMap.SimpleEntry<>("Constructed Addons", AddonManager.getInstance().getContainers().stream()
//                .filter((container) -> container.getJavaAddon() != null)
//                .map(AddonContainer::getAddonId)
//                .collect(Collectors.joining(", "))));

        if (entries.size() > 0) {
            sb.append("Details:").append(System.lineSeparator());
            for (AbstractMap.SimpleEntry<String, String> entry : entries) {
                sb.append("  ").append(entry.getKey());
                sb.append(": ");
                sb.append(entry.getValue());
                sb.append("\r\n");
            }
        }

        for (CrashCategory category : categories) {
            cs.append(System.lineSeparator()).append("=------------------------------------------------------------------=");
            cs.append(SystemUtils.LINE_SEPARATOR).append(category.toString());
        }

        cs.append("=------------------------------------------------------------------=");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        return ">>> C R A S H   R E P O R T <<<\r\n" + s1 + "\r\n" + sw + cs + "\r\n" + sb;
    }

    public String getDefaultFileName() {
        LocalDateTime now = LocalDateTime.now();
        return "Crash [" + now.format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH.mm.ss")) + "].txt";
    }

    public void defaultSave() {
        File file = new File("Game-Crashes");
        if (!file.exists()) {
            try {
                Files.createDirectories(file.toPath());
            } catch (IOException e) {
                throw new IOError(e);
            }
        }

        writeToFile(new File(file, getDefaultFileName()));
    }

    public void writeToFile(File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(toString().getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
