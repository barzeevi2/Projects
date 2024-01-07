const { test, expect } = require('@playwright/test')
import { LoginPage } from '../pages/login'

test('testLogin', async ({ page }) => {
    const Login = new LoginPage(page);
    await page.goto('https://the-internet.herokuapp.com/login');
    // await page.getByLabel('Username').click();
    // await page.getByLabel('Username').fill('tomsmith');
    // await page.getByLabel('Password').click();
    // await page.getByLabel('Password').fill('SuperSecretPassword!');
    // await page.getByRole('button', { name: ' Login' }).click();
    await Login.login('tomsmith', 'SuperSecretPassword');
});