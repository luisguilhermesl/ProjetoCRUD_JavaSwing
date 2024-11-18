package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

public class Backup {
    private static final SimpleDateFormat dataHora = new SimpleDateFormat("ddMMyyyy_HHmmss");

    private String pathAbsolutoParcial() {
        File file = new File("Backup.java");
        String pathAbsolutoAtual = file.getAbsolutePath();
        return pathAbsolutoAtual.substring(0, pathAbsolutoAtual.lastIndexOf('\\'));
    }

    public ArrayList<String> listarArquivos() {
        String pathDiretorio = pathAbsolutoParcial();
        File diretorio = new File(pathDiretorio);
        ArrayList<String> arquivosBackups = new ArrayList<>();

        if (diretorio.exists()) {
            File[] arquivosDiretorio = diretorio.listFiles();
            if (arquivosDiretorio != null && arquivosDiretorio.length > 0) {
                for (File arquivo : arquivosDiretorio) {
                    if (arquivo.isFile() && arquivo.getName().contains("backup")) {
                        arquivosBackups.add(arquivo.getAbsolutePath());
                    }
                }
            }
        }
        return arquivosBackups;
    }

    public void gerarBackup() {
        StringBuilder pathDiretorio = new StringBuilder(pathAbsolutoParcial());
        pathDiretorio.append("\\resources");
        StringBuilder zipPath = new StringBuilder(pathDiretorio).append("\\backup").append(dataHora.format(new Date())).append(".zip");

        try (FileOutputStream fos = new FileOutputStream(zipPath.toString());
             ZipOutputStream zip = new ZipOutputStream(fos)) {
            adicionarAoZip("", pathDiretorio.toString(), zip);
            JOptionPane.showMessageDialog(null, "Backup gerado com sucesso");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarAoZip(String caminhoZip, String diretorioPath, ZipOutputStream zip) throws IOException {
        File diretorio = new File(diretorioPath);
        for (String nomeArquivo : diretorio.list()) {
            String caminhoCompletoArquivo = diretorioPath + "/" + nomeArquivo;
            if (new File(caminhoCompletoArquivo).isDirectory()) {
                adicionarAoZip(caminhoZip + nomeArquivo + "/", caminhoCompletoArquivo, zip);
                continue;
            }
            try (FileInputStream fileInputStream = new FileInputStream(caminhoCompletoArquivo)) {
                zip.putNextEntry(new ZipEntry(caminhoZip + nomeArquivo));
                byte[] buffer = new byte[1024];
                int i;
                while ((i = fileInputStream.read(buffer)) > 0) {
                    zip.write(buffer, 0, i);
                }
            }
        }
    }

    public void restaurarBackup(String caminhoArquivoZip) throws FileNotFoundException, IOException {
        byte[] buffer = new byte[1024];
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(caminhoArquivoZip))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String nomeArquivo = entry.getName();
                File arquivo = new File(pathAbsolutoParcial() + "\\resources" + File.separator + nomeArquivo);
                if (entry.isDirectory()) {
                    arquivo.mkdirs();
                    continue;
                }
                File parent = arquivo.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                try (FileOutputStream fileOutputStream = new FileOutputStream(arquivo)) {
                    int i;
                    while ((i = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, i);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Backup restaurado com sucesso");
        }
    }
}
