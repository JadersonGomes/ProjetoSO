/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import DAO.Processo;
import FilaDinamica.Fila;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lab206
 */
public class Processamento {
    
    static Fila aguardando = new Fila();
    static Processo processando = null;
    static Fila  finalizados = new Fila();
    static int quantumAtual = 0;
    static Fila emIO = new Fila();
    static String stringToPrintTempo = "";
    static String stringToPrintFila = "";
    static String stringToPrintCPU = "";
    static boolean ioCalled = false;
    static Fila chegada = new Fila();
    
    
    /***
     * @see Processamento
     * @author Grupo
     * @since 2017/11/27
    */
    public Processamento()
    {
        aguardando = new Fila();
        finalizados = new Fila();
        chegada = new Fila();
    }
    
    //Inicio do processamento
    public static Fila rr(int quantum)
    {
        
        quantumAtual = quantum;
        String stringToPrintTempoFim = "";
        int quantumTotal = calculaQuantumTotal(chegada);
        Processo processoAguarda = null;
        Processo processoFinalizado = null;
        
        //FOR para iterar todos os tempos
        for(int i = 0; i < quantumTotal; i++)
        {
            stringToPrintCPU = "CPU: ";
            stringToPrintFila = "";
            stringToPrintTempo = "No tempo " + i + " ";
            quantumAtual--;
            ioCalled = false;
            
            
            //Função para montar as impressoes
            if(!stringToPrintTempoFim.equals(""))
            {
                stringToPrintTempo += stringToPrintTempoFim;
                stringToPrintTempoFim = "";
                if(processoAguarda != null)
                {
                    aguardando.enqueue(processoAguarda);
                }
                if(processoFinalizado != null)
                {
                    FinalizaProcesso(processoFinalizado);
                }
                processoAguarda = null;
                processoFinalizado = null;
            }
            
            //Adiciona os processos que chegaram no tempo atual a fila de aguardando
            chegada = AddProcessoByAT(chegada, i);
            
            
            //Valida se tem algum processo processando neste tempo
            if(processando == null)
            {
                //Caso não tenha, ele vai ficar aqui até conseguir processar alguem
                quantumAtual = quantum;
                while(processando == null)
                {
                    //Pega um processo da fila aguardando
                    AguardandoToCPU();
                    //processando = aguardando.dequeue();
                    
                    //Valida se conseguiu tirar alguem da fila de aguardando
                    if(processando != null)
                    {
                        //Valida se é momento para esse processo fazer IO
                        if(isIOTime(processando, (processando.getBtTotal() - processando.getBt())))
                        {
                            emIO.enqueue(processando);
                            processando = null;
                            quantumAtual = quantum - 1;
                        }
                        else
                        {
                            stringToPrintCPU += " " + processando.getId() + "(" + processando.getBt() + ") ";
                            processando.setBt(processando.getBt()-1);
                        }
                    }
                }
                
                //Valida se o processo já foi processado totalmente ou se ainda tem que processar
                if(processando != null && processando.getBt() == 0)
                {
                    stringToPrintTempoFim += "fim do processo " + processando.getId();
                    quantumAtual = quantum;
                    processoFinalizado = processando;
                    //finalizados.enqueue(processando);
                    processando = null;
                }  
                
            }
            else //Caso ele tenha um processo no estado de processando
            {
                
                boolean control = false;
                do
                {
                    if(control)
                        control = false;
                    
                    //Valida se tem algum processo no estado de processando
                    if(processando == null)
                    {
                        AguardandoToCPU();
                        //processando = aguardando.dequeue();
                        if(processando == null)
                        {
                            processando = emIO.dequeue();

                        }
                    }
                        
                    
                    //Caso ele tenha um processo
                    if(processando != null)
                    {
                        //Valida se tem que fazer IO
                        if(isIOTime(processando, (processando.getBtTotal() - processando.getBt())))
                        {
                            emIO.enqueue(processando);
                            processando = null;
                            quantumAtual = quantum;
                            control = true;
                        }
                        else //Caso contrario
                        {
                            //Verifique se ele já processou o total de quantuns
                            if(quantumAtual == 0)
                            {
                                stringToPrintTempo += "fim do quantum " + processando.getId();
                                //stringToPrintTempoFim += "fim do quantum " + processando.getId();
                                stringToPrintTempoFim += " ";
                                processoAguarda = processando;
                                //aguardando.enqueue(processando);
                                processando = null;
                                quantumAtual = quantum - 1;
                                control = true;
                            }
                            else
                            {
                                //Se não, ele processa
                                stringToPrintCPU += " " + processando.getId() + "(" + processando.getBt() + ") ";
                                processando.setBt(processando.getBt()-1);
                                //Valida se ainda tem quantons a serem processados
                                if(processando.getBt() == 0)
                                {
                                    stringToPrintTempoFim += "fim do processo " + processando.getId();
                                    processoFinalizado = processando;
                                    //finalizados.enqueue(processando);
                                    processando = null;
                                    quantumAtual = quantum;
                                    control = false;
                                }
                            }
                            
                        }
                    }
                    else
                    {
                        control = false;
                    }
                     
                    
                    
                }while(control);
            }
            
            //Coloca todo mundo que está em IO na fila de aguardando
            //while(!emIO.isEmpty())
            //{
            //    aguardando.enqueue(emIO.dequeue());
            //}
            VoltaAguardando();
            
            
            //Monta as informações que serão impressas
            String filaString = "FILA: ";
            Fila aguardandoCopy = aguardando.copy();
            if(!aguardandoCopy.isEmpty())
            {
                while(!aguardandoCopy.isEmpty())
                {
                    Processo p1 = aguardandoCopy.dequeue();
                    filaString += p1.getId() + "(" + p1.getBt() + ") ";
                }
                if(processoAguarda != null)
                {
                    filaString += processoAguarda.getId() + "(" + processoAguarda.getBt() + ") ";
                }
            }
            else
            {
                filaString += " Nao ha processos na fila";
            }
            stringToPrintFila += filaString;

            //Imprime
            System.out.println(stringToPrintTempo);
            System.out.println(stringToPrintFila);
            System.out.println(stringToPrintCPU);
            System.out.println("");
            addWT();
        }
        
        return null;
    }
    
    public static void VoltaAguardando()
    {
        //Coloca todo mundo que está em IO na fila de aguardando
        while(!emIO.isEmpty())
        {
            aguardando.enqueue(emIO.dequeue());
        }
    }
    
    
    
    public static void AguardandoToCPU()
    {
        processando = aguardando.dequeue();
    }
    
    public static void FinalizaProcesso(Processo p)
    {
        finalizados.enqueue(p);
    }
    
    public static int calculaQuantumTotal(Fila processos)
    {
        Fila processosCopy = processos.copy();
        int retorno = 0;
        while(!processosCopy.isEmpty())
        {
            retorno += processosCopy.dequeue().getBt();
        }
        return retorno;
    }
    
    public static Fila AddProcessoByAT(Fila fila, int ATval)
    {
        String auxString = "chegado do(s) processo(s): ";
        
        boolean auxToPrint = false;
        Fila filaCopy = fila.copy();
            
        if(!fila.isEmpty())
        {
            while(!filaCopy.isEmpty())
            {
                Processo valida = filaCopy.dequeue();
                if(valida.getAt() == ATval)
                {
                    auxString += valida.getId() +" ";
                    aguardando.enqueue(fila.get(valida));
                    auxToPrint = true;
                }
            }
        }
        
        if(auxToPrint)
            stringToPrintTempo += auxString;
            
        
        
        return fila;
    }
    
    public static void addWT()
    {
        Fila filaAux = new Fila();
        while(!aguardando.isEmpty())
        {
            Processo p = aguardando.dequeue();
            p.setWt(p.getWt()+1);
            filaAux.enqueue(p);
        }
        
        while(!emIO.isEmpty())
        {
            Processo p = emIO.dequeue();
            p.setWt(p.getWt()+1);
            aguardando.enqueue(p);
        }
        aguardando = filaAux;
    }
    
    public static boolean isIOTime(Processo processo, int bt)
    {
        String auxString = " operação do I/O ";
        boolean auxToPrint = false;
        int[] IOTimes = processo.getIo();
        if(IOTimes != null)
        {
            for(int i = 0; i < IOTimes.length; i++)
            {
                if(IOTimes[i] == bt)
                {
                    if(!ioCalled)
                    {
                        ioCalled = true;
                        stringToPrintTempo += auxString;
                    }
                    stringToPrintTempo += processo.getId() + " ";
                    IOTimes[i] = -1;
                    return true;
                }

            }
        }

        return false;
    }

    public Processo addAtributoProcesso(String string, int parseInt, int parseInt0, int[] vetorIO) {
        
        Processo p = new Processo();
        p.setId(string);
        p.setBt(parseInt);
        p.setAt(parseInt0);
        p.setBtTotal(parseInt);
        p.setIO(vetorIO);
        
        //chegada.enqueue(p);
        return p;
    }
    
    public Processo AddProcessoChegada(Processo p) {
        
        chegada.enqueue(p);
        
        return p;
    }
    
}
