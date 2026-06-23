import { useState } from 'react'

function TransferForm({ onTransfer }) {
  const [receiverWalletId, setReceiverWalletId] = useState('')
  const [amount, setAmount] = useState('')

  function handleSubmit(event) {
    event.preventDefault()

    if (!receiverWalletId || !amount) {
      return
    }

    onTransfer(Number(receiverWalletId), Number(amount))
    setReceiverWalletId('')
    setAmount('')
  }

  return (
    <form className="action-form" onSubmit={handleSubmit}>
      <h2>Send money</h2>

      <label>
        <span>Receiver wallet</span>
        <input
          type="number"
          placeholder="Enter receiver wallet id"
          value={receiverWalletId}
          onChange={(event) => setReceiverWalletId(event.target.value)}
        />
      </label>

      <label>
        <span>Amount</span>
        <input
          type="number"
          placeholder="Enter amount"
          value={amount}
          onChange={(event) => setAmount(event.target.value)}
        />
      </label>

      <button type="submit">Transfer</button>
    </form>
  )
}

export default TransferForm