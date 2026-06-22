const API_URL = 'http://localhost:8080/api'

export async function getWalletTransactions(walletId) {
  const response = await fetch(`${API_URL}/wallets/${walletId}/transactions`)

  if (!response.ok) {
    throw new Error('Error loading wallet transactions')
  }

  return response.json()
}

export async function depositToWallet(walletId, amount) {
  const response = await fetch(`${API_URL}/wallets/${walletId}/deposit`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ amount }),
  })

  if (!response.ok) {
    throw new Error('Error depositing money')
  }

  return response.json()
}