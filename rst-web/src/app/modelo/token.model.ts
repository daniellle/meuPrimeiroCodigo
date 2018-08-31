export class Token {

  access_token: string;
  expires_in: number;
  token_type: string;
  refresh_token: string;

  constructor(access_token?: string, refresh_token?: string, token_type?: string, expires_in?: number) {
    this.access_token = access_token;
    this.refresh_token = refresh_token;
    this.expires_in = expires_in;
    this.token_type = token_type;
  }

}
