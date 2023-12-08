package songjaeuk.carrot.config.auth.provider;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
}
