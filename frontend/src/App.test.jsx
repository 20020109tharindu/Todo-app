import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import App from './App';

global.fetch = vi.fn();

describe('App Component', () => {
  beforeEach(() => {
    fetch.mockClear();
  });

  it('renders the app title', () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<App />);
    expect(screen.getByText('Todo Task Manager')).toBeInTheDocument();
  });

  it('displays empty state when no tasks', async () => {
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => [],
    });

    render(<App />);
    
    await waitFor(() => {
      expect(screen.getByText('No tasks yet')).toBeInTheDocument();
    });
  });

  it('creates a new task', async () => {
    fetch
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [],
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => ({ id: 1, title: 'Test Task', description: '', completed: false }),
      })
      .mockResolvedValueOnce({
        ok: true,
        json: async () => [{ id: 1, title: 'Test Task', description: '', completed: false, createdAt: new Date().toISOString() }],
      });

    render(<App />);

    const titleInput = screen.getByPlaceholderText('Enter task title');
    const submitButton = screen.getByText('Add Task');

    fireEvent.change(titleInput, { target: { value: 'Test Task' } });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledTimes(3);
    });
  });
});