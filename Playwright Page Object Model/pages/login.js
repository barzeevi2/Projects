export class LoginPage {


    constructor(page) {
        this.page = page;

        this.username_textbox = page.getByLabel('Username');
        this.password_textbox = page.getByLabel('Password');
        this.login_button = page.getByRole('button', { name: ' Login' });
    }

    enterUsername() {

    }

    enterPassword() {

    }

    clickOnLogin() {

    }

    async login(username, password) {
        await this.username_textbox.fill(username);
        await this.password_textbox.fill(password);
        await this.login_button.click();
    }

}