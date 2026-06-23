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
    <form className="action-form" onSubmit={handleSubmit}>
      <h2>Add money</h2>

      <label>
        <span>Amount</span>
        <input
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
        />
      </label>

      <button type="submit">Deposit</button>
    </form>
  )
}

export default DepositForm