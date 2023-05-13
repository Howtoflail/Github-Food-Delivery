package food.delivery.business;

import food.delivery.domain.AccessToken;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
