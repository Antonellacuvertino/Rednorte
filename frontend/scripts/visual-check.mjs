import { mkdir } from 'node:fs/promises';
import { chromium } from 'playwright-core';

const browser = await chromium.launch({
  executablePath: 'C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe',
  headless: true
});

await mkdir('../docs/evidencias', { recursive: true });

async function login(page) {
  await page.goto('http://127.0.0.1:5173');
  await page.getByLabel(/correo/i).fill('medico@redsalud.cl');
  await page.getByLabel(/contrasena/i).fill('salud1234');
  await page.getByRole('button', { name: /entrar al panel/i }).click();
  await page.getByRole('button', { name: 'Citas' }).waitFor();
}

async function measure(page, name) {
  const dimensions = await page.evaluate(() => ({
    viewport: document.documentElement.clientWidth,
    content: document.documentElement.scrollWidth
  }));
  console.log(`${name}: ${JSON.stringify(dimensions)}`);
}

const desktop = await browser.newPage({ viewport: { width: 1440, height: 1100 } });
await login(desktop);
await desktop.getByRole('button', { name: 'Citas' }).click();
await desktop.waitForTimeout(1500);
await desktop.screenshot({ path: '../docs/evidencias/interfaz-citas-desktop.png', fullPage: true });
await measure(desktop, 'desktop');

const mobile = await browser.newPage({ viewport: { width: 390, height: 844 } });
await login(mobile);
await mobile.getByRole('button', { name: 'Configuracion' }).click();
await mobile.waitForTimeout(500);
await mobile.screenshot({ path: '../docs/evidencias/interfaz-configuracion-mobile.png', fullPage: true });
await measure(mobile, 'mobile');

await browser.close();
