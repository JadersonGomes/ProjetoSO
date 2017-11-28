package CSV;

import DAO.Processo;
import FilaDinamica.Fila;
import Negocio.Processamento;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class openCSV {

    public void abrirArquivo() {

        String caminho = "";
        

        JFileChooser abrir = new JFileChooser();
        int retorno = abrir.showOpenDialog(null);
        if (retorno == JFileChooser.APPROVE_OPTION) {

            caminho = abrir.getSelectedFile().getAbsolutePath();
        }

        File arquivoCSV = new File(caminho);
        String linhas;

        Processamento p = new Processamento();
        Processo processo = new Processo();
        Fila f = new Fila();

        try {

            Scanner leitor = new Scanner(arquivoCSV);

            while (leitor.hasNext()) {
                int cont = 0;
                linhas = leitor.nextLine();

                String[] coluna = linhas.split(";");

                int[] vetorIO = new int[coluna.length - 3];
                
                
                if((coluna.length - 3) > 0)
                {
                    while (cont < (coluna.length - 3)) {
                        vetorIO[cont] = Integer.parseInt(coluna[cont + 3]);
                        cont++;
                    }
                }
                   
                p.AddProcessoChegada(p.addAtributoProcesso(coluna[0], Integer.parseInt(coluna[1]), Integer.parseInt(coluna[2]), vetorIO));
            }

            Processamento.rr(4);

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
        }

    }

    


    
}
