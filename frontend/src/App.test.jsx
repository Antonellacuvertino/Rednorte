import { render, screen, waitFor } from '@testing-library/react';
import { beforeEach, expect, test, vi } from 'vitest';
import App from './App';

beforeEach(() => {
  localStorage.clear();
  global.fetch = vi.fn(() =>
    Promise.resolve({
      ok: true,
      json: () => Promise.resolve([])
    })
  );
});

test('muestra el acceso privado de Hospital Red Norte', async () => {
  render(<App />);
  await waitFor(() => expect(screen.getByText(/Panel clinico Hospital Red Norte/i)).toBeTruthy());
  expect(screen.getByText(/Registrarse/i)).toBeTruthy();
});
