import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class EmergencySimulation {
    private static final int LotacaoMaximaEspera = 50;
    private static final int LotacaoMaximaPreferencial = 10;
    private static final int MinimoTempoTriagem = 1;
    private static final int TempoMaximoTriagem = 3;
    private static final int TempoMinimoTratamento = 2;
    private static final int TempoMaximoTratamento = 5;
    private static final int TempoMaximoEspera = 50;

    private Queue<Patient> SalaDeEspera;
    private Queue<Patient> FilaVermelha;
    private Queue<Patient> FilaAmarela;
    private Queue<Patient> FilaVerde;
    private Queue<Patient> FilaAzul;
    private int interruptions;

    public EmergencySimulation() {
        SalaDeEspera = new LinkedList<>();
        FilaVermelha = new LinkedList<>();
        FilaAmarela = new LinkedList<>();
        FilaVerde = new LinkedList<>();
        FilaAzul = new LinkedList<>();
        interruptions = 0;
    }

    public void simulate(int rounds) {
        for (int i = 1; i <= rounds; i++) {
            ingresso();
            triagem();
            atendimento();
        }

        System.out.println("Quantidade de pacientes que não ficaram por lotação na sala de espera: " + SalaDeEspera.size());
        System.out.println("Tempo médio de espera na sala de espera: " + CalculoTempoFilas(SalaDeEspera));
        System.out.println("Tempo médio de espera na fila vermelha: " + CalculoTempoFilas(FilaVermelha));
        System.out.println("Tempo médio de espera na fila amarela: " + CalculoTempoFilas(FilaAmarela));
        System.out.println("Tempo médio de espera na fila verde: " + CalculoTempoFilas(FilaVerde));
        System.out.println("Tempo médio de espera na fila azul: " + CalculoTempoFilas(FilaAzul));
        System.out.println("Número de interrupções no ingresso: " + interruptions);
    }

    private void ingresso() {
        Random random = new Random();
        if (random.nextDouble() < 0.5) {
            if (SalaDeEspera.size() < LotacaoMaximaEspera) {
                SalaDeEspera.offer(new Patient());
            } else {
                System.out.println("Sala de espera cheia, paciente foi embora.");
            }
        }
    }

    private void triagem() {
        Queue<Patient> tempQueue = new LinkedList<>();
    
        for (Patient patient : SalaDeEspera) {
            int triageTime = getTempoAleatorio(MinimoTempoTriagem, TempoMaximoTriagem);
            patient.setTriageTime(triageTime);
            patient.setWaitingTime(patient.getWaitingTime() + triageTime);
            
            Risco riskLevel = patient.getRiscoAleatorio();
            
            if (riskLevel == Risco.VERMELHO) {
                FilaVermelha.offer(patient);
            } else if (riskLevel == Risco.AMARELO) {
                FilaAmarela.offer(patient);
            } else if (riskLevel == Risco.VERDE) {
                FilaVerde.offer(patient);
            } else if (riskLevel == Risco.AZUL) {
                FilaAzul.offer(patient);
            }
            
            tempQueue.offer(patient);
        }
    
        SalaDeEspera.clear();
        SalaDeEspera.addAll(tempQueue);
    }
    

    private void atendimento() {
        int preferredPatientsCount = 0;

        for (int i = 0; i < LotacaoMaximaPreferencial; i++) {
            if (!FilaVermelha.isEmpty()) {
                Patient patient = FilaVermelha.poll();
                int treatmentTime = getTempoAleatorio(TempoMinimoTratamento, TempoMaximoTratamento);
                patient.setWaitingTime(patient.getWaitingTime() + treatmentTime);
                if (patient.getWaitingTime() > TempoMaximoEspera) {
                    System.out.println("Paciente " + patient.getId() + " ultrapassou o tempo máximo de espera e foi encaminhado para atendimento preferencial.");
                    preferredPatientsCount++;
                } else {
                    System.out.println("Paciente " + patient.getId() + " foi atendido e encaminhado para internação ou liberado para casa.");
                }
            } else if (!FilaAmarela.isEmpty()) {
                Patient patient = FilaAmarela.poll();
                int treatmentTime = getTempoAleatorio(TempoMinimoTratamento, TempoMaximoTratamento);
                patient.setWaitingTime(patient.getWaitingTime() + treatmentTime);
                if (patient.getWaitingTime() > TempoMaximoEspera) {
                    System.out.println("Paciente " + patient.getId() + " ultrapassou o tempo máximo de espera e foi encaminhado para atendimento preferencial.");
                    preferredPatientsCount++;
                } else {
                    System.out.println("Paciente " + patient.getId() + " foi atendido e encaminhado para internação ou liberado para casa.");
                }
            } else if (!FilaVerde.isEmpty()) {
                Patient patient = FilaVerde.poll();
                int treatmentTime = getTempoAleatorio(TempoMinimoTratamento, TempoMaximoTratamento);
                patient.setWaitingTime(patient.getWaitingTime() + treatmentTime);
                if (patient.getWaitingTime() > TempoMaximoEspera) {
                    System.out.println("Paciente " + patient.getId() + " ultrapassou o tempo máximo de espera e foi encaminhado para atendimento preferencial.");
                    preferredPatientsCount++;
                } else {
                    System.out.println("Paciente " + patient.getId() + " foi atendido e encaminhado para internação ou liberado para casa.");
                }
            } else if (!FilaAzul.isEmpty()) {
                Patient patient = FilaAzul.poll();
                int treatmentTime = getTempoAleatorio(TempoMinimoTratamento, TempoMaximoTratamento);
                patient.setWaitingTime(patient.getWaitingTime() + treatmentTime);
                if (patient.getWaitingTime() > TempoMaximoEspera) {
                    System.out.println("Paciente " + patient.getId() + " ultrapassou o tempo máximo de espera e foi encaminhado para atendimento preferencial.");
                    preferredPatientsCount++;
                } else {
                    System.out.println("Paciente " + patient.getId() + " foi atendido e encaminhado para internação ou liberado para casa.");
                }
            } else {
                break; // Todas as filas estão vazias, não há mais pacientes para atender
            }
        }

        if (preferredPatientsCount > LotacaoMaximaPreferencial) {
            System.out.println("Interrupção no ingresso: " + (preferredPatientsCount - LotacaoMaximaPreferencial) + " pacientes foram encaminhados para outras unidades de atendimento.");
            interruptions++;
        }
    }

    private int getTempoAleatorio(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    private double CalculoTempoFilas(Queue<Patient> queue) {
        int TempoTotalDeEspera = 0;
        int TotalPacientes = queue.size();
        for (Patient patient : queue) {
            TempoTotalDeEspera += patient.getWaitingTime();
        }
        return (double) TempoTotalDeEspera / TotalPacientes;
    }
}

