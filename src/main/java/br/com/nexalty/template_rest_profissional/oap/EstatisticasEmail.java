package br.com.nexalty.template_rest_profissional.oap;
public class EstatisticasEmail {
    private final Long totalEmails;
    private final Long emailsLidos;
    private final Long emailsNaoLidos;
    
    public EstatisticasEmail(Long totalEmails, Long emailsLidos, Long emailsNaoLidos) {
        this.totalEmails = totalEmails;
        this.emailsLidos = emailsLidos;
        this.emailsNaoLidos = emailsNaoLidos;
    }
    
    // Getters
    public Long getTotalEmails() { return totalEmails; }
    public Long getEmailsLidos() { return emailsLidos; }
    public Long getEmailsNaoLidos() { return emailsNaoLidos; }
    
    public Double getPercentualLidos() {
        return totalEmails > 0 ? (emailsLidos.doubleValue() / totalEmails.doubleValue()) * 100 : 0.0;
    }
    
    @Override
    public String toString() {
        return String.format("Estatísticas: Total=%d, Lidos=%d (%.1f%%), Não Lidos=%d", 
                totalEmails, emailsLidos, getPercentualLidos(), emailsNaoLidos);
    }
}