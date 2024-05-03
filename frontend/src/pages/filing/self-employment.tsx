import {
  Form,
  Grid,
  Label,
  Button,
  Fieldset,
  TextInput,
  ButtonGroup,
  GridContainer,
  StepIndicator,
  TextInputMask,
  StepIndicatorStep,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { useTranslation } from "react-i18next";
import { Link, useNavigate } from "react-router-dom";

export default function SelfEmployment() {
  const { t } = useTranslation();
  const navigate = useNavigate();

  const handleSelfEmploymentInfo = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = {
      // @ts-expect-error untyped form elements but we need the values
      payerTIN: e.currentTarget.elements.payer_tin.value,
      // @ts-expect-error untyped form elements but we need the values
      recipientTIN: e.currentTarget.elements.recipient_tin.value,
      // @ts-expect-error untyped form elements but we need the values
      nec: e.currentTarget.elements.nec.value,
      // @ts-expect-error untyped form elements but we need the values
      withheld: e.currentTarget.elements.withheld.value,
    };

    console.log(formData);
    navigate("/filing/deductions");
  };

  return (
    <main className="full-page">
      <GridContainer className="usa-section">
        <StepIndicator headingLevel="h3" ofText="of" stepText="Step">
          <StepIndicatorStep label={t("personal.title")} status="complete" />
          <StepIndicatorStep label={t("filing-info.title")} status="complete" />
          <StepIndicatorStep label={t("w2.title")} status="complete" />
          <StepIndicatorStep label={t("1099.title")} status="current" />
          <StepIndicatorStep label={t("deductions.title")} />
          <StepIndicatorStep label="Review" />
          <StepIndicatorStep label="Results" />
        </StepIndicator>

        <Form onSubmit={handleSelfEmploymentInfo} className="w-full">
          <Fieldset legend={t("1099.desc")}>
            {/* Payer and Recipient TIN */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label id="label-payer-tin" htmlFor="payer_tin" requiredMarker>
                  {t("1099.payerTIN")}
                </Label>
                <TextInputMask
                  id="payer_tin"
                  name="payer_tin"
                  type="text"
                  aria-labelledby="label-payer-tin"
                  mask="__-_______"
                  pattern="^\d{2}-\d{7}"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label
                  id="label-recipient-tin"
                  htmlFor="recipient_tin"
                  requiredMarker
                >
                  {t("1099.recipientTIN")}
                </Label>
                <TextInputMask
                  id="recipient_tin"
                  name="recipient_tin"
                  type="text"
                  aria-labelledby="label-recipient-tin"
                  mask="___-__-____"
                  pattern="\d{3}-\d{2}-\d{4}"
                  required
                />
              </Grid>
            </Grid>

            {/* NEC */}
            <Grid row gap>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="label-nec" requiredMarker>
                  {t("1099.nec")}
                </Label>
                <span id="hint-nec" className="usa-hint">
                  {t("1099.hint", { hint: "1" })}
                </span>
                <TextInput
                  id="nec"
                  name="nec"
                  type="number"
                  aria-describedby="hint-nec"
                  aria-labelledby="label-nec"
                  required
                />
              </Grid>

              <Grid tablet={{ col: true }}>
                <Label id="label-withheld" htmlFor="withheld" requiredMarker>
                  {t("w2.federal-withheld")}
                </Label>
                <span id="hint-withheld" className="usa-hint">
                  {t("1099.hint", { hint: "4" })}
                </span>
                <TextInput
                  id="withheld"
                  name="withheld"
                  type="number"
                  aria-describedby="hint-withheld"
                  aria-labelledby="label-withheld"
                  required
                />
              </Grid>
            </Grid>

            <div className="tablet:display-flex tablet:flex-justify">
              <ButtonGroup type="default">
                <Link
                  to="/filing/w2"
                  className="usa-button usa-button--outline"
                >
                  {t("back")}
                </Link>
                <Link
                  to="/filing/deductions"
                  className="usa-button usa-button--base"
                >
                  {t("skip")}
                </Link>
                <Button type="submit">{t("continue")}</Button>
              </ButtonGroup>
            </div>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
