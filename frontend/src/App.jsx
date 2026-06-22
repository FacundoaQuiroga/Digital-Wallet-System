import { useEffect, useState } from 'react'
import './App.css'
import TransactionList from './components/TransactionList'
import { depositToWallet,getWalletTransactions,transferMoney } from './services/walletApi'
import DepositForm from './components/DepositForm'
import TransferForm from './components/TransferForm'


function App() {
  const [transactions, setTransactions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    loadTransactions()
  }, [])

function loadTransactions() {
  setLoading(true)
  setError('')

  getWalletTransactions(1)
    .then((data) => {
      setTransactions(data)
    })
    .catch(() => {
      setError('Could not load transactions.')
    })
    .finally(() => {
      setLoading(false)
    })
}

function handleDeposit(amount) {
  depositToWallet(1, amount)
    .then(() => {
      loadTransactions()
    })
    .catch(() => {
      setError('Could not deposit money.')
    })
}

function handleTransfer(receiverWalletId, amount) {
  transferMoney(1, receiverWalletId, amount)
    .then(() => {
      loadTransactions()
    })
    .catch(() => {
      setError('Could not transfer money.')
    })
}

  return (
    <>
      <h1>Digital Wallet System</h1>

        <DepositForm onDeposit={handleDeposit} />
        <TransferForm onTransfer={handleTransfer} />
        
      <section>
        <h2>Wallet #1 transactions</h2>

        {loading && <p>Loading...</p>}

        {error && <p>{error}</p>}

        {!loading && !error && (
          <TransactionList transactions={transactions} />
        )}
      </section>
    </>
  )
}

export default App