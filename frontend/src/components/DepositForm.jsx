import { useState } from 'react'

function DepositForm({ onDeposit }) {
  const [amount, setAmount] = useState('')

  function handleSubmit(event) {
    event.preventDefault()

    if (!amount) {
      return
    }

    onDeposit(Number(amount))
    setAmount('')
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Deposit money</h2>

      <input
        type="number"
        placeholder="Amount"
        value={amount}
        onChange={(event) => setAmount(event.target.value)}
      />

      <button type="submit">Deposit</button>
    </form>
  )
}

export default DepositForm